package com.example.poppop.domain.bookmark.dto.response;

import com.example.poppop.domain.popup.entity.Popup;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public record PopupBookmarkResponse(
        Long popupId,
        String title,
        String image,
        String location,
        LocalDate startDate,
        LocalDate endDate,
        String imageUrl,
        long daysToStart
) {
    public static PopupBookmarkResponse from(Popup popup) {

        long dayToStart = ChronoUnit.DAYS.between(LocalDate.now(), popup.getStartDate());
        // 3일 남았으면 +3, 3일 지났으면 -3
        int dDay;
        if (dayToStart > 0) {
            dDay = (int) dayToStart;   // 오픈까지 남은 일수: +N
        } else if (dayToStart < 0) {
            dDay = (int) dayToStart;   // 오픈 후 경과 일수: -N
        } else {
            dDay = 0;            // 오늘 오픈: 0
        }

        return new PopupBookmarkResponse(
                popup.getId(),
                popup.getTitle(),
                popup.getImage(),
                popup.getLocation(),
                popup.getStartDate(),
                popup.getEndDate(),
                popup.getImage(),
                dayToStart
        );
    }
}
