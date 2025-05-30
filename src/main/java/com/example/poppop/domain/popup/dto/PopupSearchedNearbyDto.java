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

    @Builder
    public PopupSearchedNearbyDto(BigDecimal latitude, BigDecimal longitude, Long id) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.id = id;
    }

    public static PopupSearchedNearbyDto from(Popup popup) {
        return PopupSearchedNearbyDto.builder()
                .id(popup.getId())
                .latitude(popup.getLatitude())
                .longitude(popup.getLongitude())
                .build();
    }
}
