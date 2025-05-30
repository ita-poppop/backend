package com.example.poppop.domain.popup.dto;

import com.example.poppop.domain.popup.entity.Popup;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

@Getter
@Slf4j
public class PopupPlannedDto {
    private long id;
    private String title;
    private String image;
    private String location; // 서울 성수동까지만
    private String dDay;

    @Builder
    public PopupPlannedDto(Long id, String title, String image, String location, String dDay) {
        this.id = id;
        this.title = title;
        this.image = image;
        this.location = location;
        this.dDay = dDay;
    }

    public static PopupPlannedDto from(Popup popup) {
        return PopupPlannedDto.builder()
                .id(popup.getId())
                .title(popup.getTitle())
                .image(popup.getImage())
                .location(extractLocation(popup.getLocation()))
                .dDay(createDday(popup.getStartDate()))
                .build();
    }

    private static String createDday(String startDate) {
        LocalDate today = LocalDate.now(); //2025-05-20
        LocalDate startDatePars = LocalDate.parse(startDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        long dDay = ChronoUnit.DAYS.between(today, startDatePars);
        return String.valueOf(dDay);
    }

    private static String extractLocation(String location) {
        // ::todo:: 파싱해서 넣어주기 indexof substring 아니면 split으로
        String[] words = location.split(" ");
        if (words.length >= 2) {
            return words[0] + " " + words[1];
        } else {
            log.info("위치 파싱 실패: {}", location);
            return location; // 또는 "" (빈 문자열)
        }
    }
}
