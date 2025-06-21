package com.example.poppop.domain.popup.dto;

import com.example.poppop.domain.popup.entity.Popup;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Slf4j
public class PopupInitialDto {
    private String title;
    private String date;
    private String comment;
    private String detail;
    private String image;
    private String location;

    @Builder
    public PopupInitialDto(String title, String date, String comment, String detail, String image, String location) {
        this.title = title;
        this.date = date;
        this.comment = comment;
        this.detail = detail;
        this.image = image;
        this.location = location;
    }

    public Popup toEntity() {
        LocalDate startDate = extractStartDate(date);
        LocalDate endDate = extractEndDate(date);

        return Popup.builder()
                .title(title)
                .startDate(startDate)
                .endDate(endDate)
                .comment(comment)
                .detail(detail)
                .image(image)
                .location(removeLocationNbsp(location))
                .build();
    }


    private LocalDate extractStartDate(String date) {
        try {
            if(date != null && !date.isEmpty()) {
                String[] splitDate = date.split("~");
                if(splitDate.length >= 1) { // ✅ 안전성 체크
                    String startDate = splitDate[0].trim();
                    return parseDate(startDate);
                }
            }
        } catch (Exception e) {
            log.error("시작 날짜 분리 실패: {}, 원본: {}", e.getMessage(), date);
        }
        return null;
    }

    private LocalDate extractEndDate(String date) {
        try {
            if(date != null && !date.isEmpty()) {
                String[] splitDate = date.split("~");
                if(splitDate.length >= 2) { // ✅ 안전성 체크
                    String endDate = splitDate[1].trim();
                    return parseDate(endDate);
                }
            }
        } catch (Exception e) {
            log.error("종료 날짜 분리 실패: {}, 원본: {}", e.getMessage(), date);
        }
        return null;
    }

    private LocalDate parseDate(String dateString) {
        if (dateString == null || dateString.trim().isEmpty()) {
            return null;
        }

        // ✅ 모든 공백 문자 제거 (정규식 사용)
        String cleanDate = dateString.replaceAll("[\\s\\u00A0\\u2000-\\u202F]+", "");

        log.info("공백 제거 후 날짜: '{}'", cleanDate);

        try {
            return LocalDate.parse(cleanDate);
        } catch (Exception e) {
            log.error("날짜 파싱 실패: '{}', 오류: {}", cleanDate, e.getMessage());
            return null;
        }
    }

    public static String removeLocationNbsp(String location) {
        return location == null ? null :
                location.replace("복사", "")
                        .replace("&nbsp;", "")
                        .replaceAll("\\s+", " ")
                        .trim();
    }
}
