package com.example.poppop.domain.popup.service;

import com.example.poppop.domain.member.entity.CustomOAuth2User;
import com.example.poppop.domain.popup.dto.PopupDetailDto;
import com.example.poppop.domain.popup.dto.PopupPlannedDto;
import com.example.poppop.domain.popup.dto.PopupSearchDto;
import com.example.poppop.domain.popup.dto.PopupTrendDto;
import com.example.poppop.domain.popup.dto.request.PopupSearchRequestDto;
import com.example.poppop.domain.popup.entity.Popup;
import com.example.poppop.domain.popup.repository.PopupRepository;
import com.example.poppop.global.error.GlobalErrorCode;
import com.example.poppop.global.error.exception.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDate;
import java.util.Arrays;
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

    // 팝업 상세 조회
    public PopupDetailDto getDetailPopup(Long id) {
        Popup DetailPopup = popupRepository.findById(id)
                .orElseThrow(() -> new CustomException(GlobalErrorCode.NOT_FOUND));
        return PopupDetailDto.from(DetailPopup);
    }
    // 팝업 조회수증가
    @Transactional
    public void incrementViewCount(Long popupId, CustomOAuth2User oAuth2User) {
        Long memberId=oAuth2User.getId();
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
    public List<PopupSearchDto> getSearchedPopups(Integer page, Integer size, PopupSearchRequestDto requestDto) {
        String title = requestDto.getTitle();
        PageRequest pageable = PageRequest.of(page - 1, size);
        List<Popup> searchedPopups = popupRepository.findSearchedPopups(pageable, title);
        return searchedPopups.stream()
                .map(PopupSearchDto::from)
                .collect(Collectors.toList());
    }
    //

}
