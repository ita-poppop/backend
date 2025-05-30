package com.example.poppop.domain.popup.service;

import com.example.poppop.domain.popup.entity.Popup;
import com.example.poppop.domain.popup.repository.PopupRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PopupGeoService {

    private final PopupRepository popupRepository;

    @Value("${naver.geocoding.client-id}")
    private String clientId;

    @Value("${naver.geocoding.client-secret}")
    private String clientSecret;

    // 위치의 위도 경도를 x와 y로 파싱해주는 로직
    public BigDecimal[] getLatLng(String address) throws UnsupportedEncodingException {
        try {
            String url = UriComponentsBuilder.fromHttpUrl("https://maps.apigw.ntruss.com/map-geocode/v2/geocode")
                    .queryParam("query", address)
                    .build().toUriString();

            HttpHeaders httpHeaders = new HttpHeaders(); // http 헤더 값 설정
            httpHeaders.set("Accept", "application/json");
            httpHeaders.set("X-NCP-APIGW-API-KEY-ID", clientId);
            httpHeaders.set("X-NCP-APIGW-API-KEY", clientSecret);

            HttpEntity<String> entity = new HttpEntity<>(httpHeaders); //HttpEntity는 HTTP 요청 또는 응답에서 헤더와 바디를 함께 담을 수 있는 스프링 프레임워크의 클래스
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class); //지정한 URL로 HTTP 요청을 보내고, 응답을 받아오는 메서드

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode root = objectMapper.readTree(response.getBody());
            JsonNode addresses = root.path("addresses");
            if (addresses != null && addresses.isArray() && addresses.size() > 0) {
                JsonNode addr = addresses.get(0);
                String x = addr.path("x").asText();
                String y = addr.path("y").asText();
                return new BigDecimal[]{new BigDecimal(x), new BigDecimal(y)};
            }
        } catch (Exception e) {
            throw new RuntimeException("네이버 Geocoding API 호출/파싱 오류", e);
        }
        return null;
    }

    // 모든 팝업의 주소를 위도 경도로 변환해 저장 최초 1회 실행하는 로직
    @Transactional
    public void batchPopup() {
        List<Popup> popups = popupRepository.findAll();
        for(Popup popup : popups){
            try {
                BigDecimal[] latLng = getLatLng(popup.getLocation());
                if (latLng != null && latLng.length > 1) {
                    popup.setLatitude(latLng[0]);
                    popup.setLongitude(latLng[1]);
                } else {
                    //log.warn("위경도 변환 실패: id={}, 주소={}", popup.getId(), popup.getLocation());
                }
            } catch (Exception e) {
                //log.error("예외 발생: id={}, 주소={}, error={}", popup.getId(), popup.getLocation(), e.getMessage());
            }
        }
        log.info("팝업 위도/경도 변환 완료");
        // 트랜잭션 종료 시 JPA Dirty Checking으로 DB에 자동 반영
    }

    //

}

