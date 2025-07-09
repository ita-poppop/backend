package com.example.poppop.domain.popup.service;

import com.example.poppop.domain.member.service.MemberService;
import com.example.poppop.domain.popup.dto.*;
import com.example.poppop.domain.popup.entity.Popup;
import com.example.poppop.domain.popup.repository.PopupRepository;
import com.example.poppop.global.auth.model.PopPopOAuth2User;
import com.example.poppop.global.auth.service.JwtService;
import com.example.poppop.global.error.GlobalErrorCode;
import com.example.poppop.global.error.exception.CustomException;
import com.google.type.Decimal;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PopupService {
    private static final String ZSET_VIEWCOUNT_KEY = "popup:viewcount";

    private final PopupRepository popupRepository;
    private final RedisTemplate<String, String> redisTemplate;
    private final PopupRedisService popupRedisService;
    private final JwtService jwtService;
    private final MemberService memberService;
    private final PopupGeoService popupGeoService;

    // 팝업 상세 조회
    public PopupDetailDto getDetailPopup(Long id) {
        Popup DetailPopup = popupRepository.findById(id)
                .orElseThrow(() -> new CustomException(GlobalErrorCode.NOT_FOUND));
        return PopupDetailDto.from(DetailPopup);
    }
    // 팝업 조회수증가
    @Transactional
    public void incrementViewCount(Long popupId, PopPopOAuth2User user) {
        Long memberId = user.getMemberId();
        String strMemberId = String.valueOf(memberId);
        String strPopupId = String.valueOf(popupId);

        String visitedPopupIds = popupRedisService.getValue(strMemberId);
        if(visitedPopupIds==null){
            popupRedisService.setDateExpire("member:"+strMemberId,strPopupId+"_",calculateTimeOut(3));
            popupRedisService.addViewCountInRedis(strPopupId);
        }else{
            String[] strArray = visitedPopupIds.split("_");
            List<String> redisPopupList = Arrays.asList(strArray);
            boolean isView = false;
            if (!redisPopupList.isEmpty()) {
                for (String redisPopupId : redisPopupList) {
                    if(strPopupId.equals(redisPopupId)){
                        isView = true;
                        break;
                    }
                }
                if(!isView){
                    popupRedisService.appendValues(strMemberId,strPopupId + "_"); // 1_2_3_
                    popupRedisService.addViewCountInRedis(strPopupId);
                }
            }
        }
    }
    //1시간에 한번씩 reids에 저장된 팝업별 조회수를 mysql에 반영하고, redis 데이터는 삭제
    @Transactional
    @Scheduled(cron = "0 0 * * * *", zone = "Asia/Seoul")
    public void syncViewCount() {
        List<String> popupIdList = popupRedisService.deleteKeyList();
        for (String popupId : popupIdList) {
            String strViewCount = popupRedisService.getAndDeleteViewCount(popupId);
            int viewCount = Integer.parseInt(strViewCount);
            Popup popup = popupRepository.findById(Long.valueOf(popupId))
                    .orElseThrow(() -> new CustomException(GlobalErrorCode.NOT_FOUND));
            popup.increaseViewCount(viewCount);
        }
    }
    // 오픈예정 팝업 조회
    // :todo: @CacheEvic 시간 남으면 추가 현재 팝업이 수정되거나 삭제되지는 않을거 같음
    @Cacheable(
            cacheNames = "plannedPopups",
            key = "'page:' + #page + ':size:' + #size"
    )
    public List<PopupPlannedDto> getPlannedPopup(Integer page, Integer size) {
        LocalDate now = LocalDate.now();
        LocalDate end = now.plusDays(3);

        PageRequest pageable = PageRequest.of(page - 1, size);
        List<Popup> plannedPopups = popupRepository.findPlannedPopups(now,end,pageable);
        return plannedPopups.stream()
                .map(PopupPlannedDto::from)
                .collect(Collectors.toList());
    }
    // 트랜드 팝업 조회
    // 레디스 저장 -> 1시간 마다 db 동기화 -> 근데 한번 조회수가 저장되고
    @Cacheable(
            cacheNames = "trendPopups",
            key = "'page:' + #page + 'size:' + #size"
    )
    public List<PopupTrendDto> getTrendPopups(Integer page, Integer size) {
        PageRequest pageable = PageRequest.of(page - 1, size);
        List<Popup> trendPopups = popupRepository.findTrendPopups(pageable);
        return trendPopups.stream()
                .map(PopupTrendDto::from)
                .collect(Collectors.toList());
    }
    //ttl 시간 설정
    private Duration calculateTimeOut(int minutes) {
        return Duration.ofMinutes(minutes);
    }

    // 검색한 팝업 조회
    public List<PopupSearchedNearbyDto> getSearchedPopups(String content, Integer page, Integer size) throws UnsupportedEncodingException {
        // 만약 db에서 찾을 수 있는 이름의 팝업이라면 db에서 찾아서 반환해주고 그게 아니라면 서울시 강남구라면
        // 받은 content(위치 주소를를 지오코딩으로 위경도를 바꿔주고 마찬가지로 팝업 반환
        Pageable pageable = PageRequest.of(page - 1, size);

        // 팝업 이름으로 검색 (LIKE 검색)
        List<Popup> searchedPopups = popupRepository.findSearchedPopups(content, pageable);

        if (searchedPopups != null && !searchedPopups.isEmpty()) {
            // 팝업 이름으로 결과가 있으면 바로 반환
            return searchedPopups.stream()
                    .map(PopupSearchedNearbyDto::from)
                    .collect(Collectors.toList());
        }

        // 결과가 없으면 주소로 간주, 지오 후 주변 팝업 검색
        BigDecimal[] coordinate = popupGeoService.getLatLng(content);
        if (coordinate != null) {
            BigDecimal lng=coordinate[0]; //경도 x축
            BigDecimal lat = coordinate[1]; //위도 y축
            log.info(lat+","+lng);
            Double radius = 3.0;
            List<Popup> popupsWithinRadius = popupRepository.findPopupsWithinRadius(lat, lng, radius, pageable);
            if (popupsWithinRadius.isEmpty()) {
                return Collections.emptyList(); // null 대신 빈 리스트 반환
            }
            return popupsWithinRadius.stream()
                    .map(PopupSearchedNearbyDto::from)
                    .collect(Collectors.toList());
        }
        //모든 경우에 대해 빈 리스트 반환 보장
        return Collections.emptyList();
    }

    //현재 위경도를 기준으로 3km내 반경에 있는 주위 팝업을 반환
    private List<Popup> findNearByPopups(Popup popup, Pageable pageable) {
        BigDecimal lng = popup.getLongitude();
        BigDecimal lat = popup.getLatitude();
        // 3km 이내 팝업 조회
        Double radius = 3.0;
        List<Popup> popupsWithin3km = popupRepository.findPopupsWithinRadius(lat, lng, radius, pageable);
        if (!popupsWithin3km.isEmpty()) {
            return popupsWithin3km;
        }
        // 5km 이내 팝업 조회 (3km 내에 없을 때만)
        Double radius5 = 5.0;
        List<Popup> popupsWithin5km = popupRepository.findPopupsWithinRadius(lat, lng, radius5,pageable);
        return popupsWithin5km; // 없으면 빈 리스트 반환
    }

    public List<PopupSearchedNearbyDto> getPopupsByLocation(BigDecimal longitude, BigDecimal latitude, Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Double radius = 3.0;
        List<Popup> popupsWithin3km = popupRepository.findPopupsWithinRadius(latitude, longitude, radius, pageable);
        if (!popupsWithin3km.isEmpty()) {
           return popupsWithin3km.stream()
                    .map(PopupSearchedNearbyDto::from)
                    .collect(Collectors.toList());
        }
        // 5km 이내 팝업 조회 (3km 내에 없을 때만)
        Double radius5 = 5.0;
        List<Popup> popupsWithin5km = popupRepository.findPopupsWithinRadius(latitude, longitude, radius5, pageable);
        if (!popupsWithin5km.isEmpty()) {
            return popupsWithin5km.stream()
                    .map(PopupSearchedNearbyDto::from)
                    .collect(Collectors.toList());
        }
        // 5km 내에도 없으면 빈 리스트 반환
        return Collections.emptyList();
    }
}