package com.example.poppop.domain.popup.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.example.poppop.domain.popup.dto.PopupInitialDto;
import com.example.poppop.domain.popup.entity.Popup;
import com.example.poppop.domain.popup.repository.PopupRepository;
import com.example.poppop.global.error.GlobalErrorCode;
import com.example.poppop.global.error.exception.CustomException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
@ConditionalOnProperty(name = "app.data-init.enabled", havingValue = "true", matchIfMissing = false)
public class PopupDataInitializer implements CommandLineRunner {
    //commandLineRunner란 스프링부트가 시작될때 자동 실행되는 인터페이스
    private final ObjectMapper objectMapper;
    private final PopupRepository popupRepository;
    private final PopupGeoService popupGeoService;
    // json 데이터를 db에 저장하는 로직
    @Override
    public void run(String... args) throws Exception {
        try {
            // 중복 실행 방지: 이미 데이터가 있으면 건너뜀
            long existingCount = popupRepository.count();
            if (existingCount > 0) {
                log.info("이미 팝업 데이터가 {}건 존재합니다. 초기화를 건너뜁니다.", existingCount);
                return;
            }

            log.info("팝업 데이터 초기화를 시작합니다.");

            InputStream inputStream = getClass()
                    .getClassLoader()
                    .getResourceAsStream("data/popup_data.json");

            if (inputStream == null) {
                log.error("[초기화 실패] popup_data.json 파일을 불러올 수 없습니다.");
                throw new CustomException(GlobalErrorCode.NOT_FOUND);
            }

            // JSON → DTO 리스트로 파싱
            List<PopupInitialDto> dtoList = objectMapper.readValue(
                    inputStream,
                    new TypeReference<List<PopupInitialDto>>() {}
            );

            // DTO → Entity 변환
            List<Popup> popups = dtoList.stream()
                    .map(PopupInitialDto::toEntity)
                    .collect(Collectors.toList());

            // DB 저장
            popupRepository.saveAll(popups);
            log.info("팝업 데이터 초기화 완료 (총 {}건)", popups.size());

            // 위도/경도 변환 배치 실행
            try {
                popupGeoService.batchPopup();
                log.info("팝업 위도/경도 변환 배치 완료");
            } catch (Exception geoException) {
                log.error("위도/경도 변환 배치 중 오류 발생: {}", geoException.getMessage());
                // 위도/경도 변환 실패해도 전체 초기화는 성공으로 처리
            }

        } catch (CustomException ce) {
            log.error("사용자 정의 예외 발생: {}", ce.getMessage());
            throw ce;
        } catch (Exception e) {
            log.error("팝업 데이터 초기화 중 예외 발생", e);
            throw new CustomException(GlobalErrorCode.INTERNAL_SERVER_ERROR,"팝업 데이터 초기화 실패");
        }
    }
}
