package com.example.poppop.domain.bookmark.dto.response;

import com.example.poppop.domain.popup.entity.Popup;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public record PopupBookmarkResponse(
        Long popupId,
        String title,
        String location,
        LocalDate startDate,
        LocalDate endDate,
        String imageUrl,
        long daysToStart
) {
    public static PopupBookmarkResponse from(Popup popup) {
        long dayToStart = ChronoUnit.DAYS.between(LocalDate.now(), popup.getStartDate());
        return new PopupBookmarkResponse(
                popup.getId(),
                popup.getTitle(),
                popup.getLocation(),
                popup.getStartDate(),
                popup.getEndDate(),
                popup.getImage(),
                dayToStart
        );
    }
}
