package com.example.poppop.domain.popup.dto;

import com.example.poppop.domain.popup.entity.Popup;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class PopupSearchedNearbyDto {
    private BigDecimal latitude;
    private BigDecimal longitude;
    private Long id;
    private String imageUrl;
    private String title;
    private String date;

    @Builder
    public PopupSearchedNearbyDto(BigDecimal latitude, BigDecimal longitude, Long id, String imageUrl, String title, String date) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.id = id;
        this.imageUrl = imageUrl;
        this.title = title;
        this.date = date;
    }

    public static PopupSearchedNearbyDto from(Popup popup) {
        return PopupSearchedNearbyDto.builder()
                .id(popup.getId())
                .latitude(popup.getLatitude())
                .longitude(popup.getLongitude())
                .title(popup.getTitle())
                .imageUrl(popup.getImage())
                .date(popup.getStartDate() + " ~ " + popup.getEndDate())
                .build();
    }
}
