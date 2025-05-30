package com.example.poppop.domain.popup.dto;

import com.example.poppop.domain.popup.entity.Popup;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PopupTrendDto {
    private String title;
    private String imageUrl;
    private String location;

    @Builder
    public PopupTrendDto(String title, String imageUrl, String location) {
        this.title = title;
        this.imageUrl = imageUrl;
        this.location = location;
    }

    public static PopupTrendDto from(Popup popup) {
        return PopupTrendDto.builder()
                .title(popup.getTitle())
                .imageUrl(popup.getImage())
                .location(extractLocation(popup.getLocation()))
                .build();
    }

    private static String extractLocation(String location) {
        String[] strArray = location.split(" ");
        String first = strArray[0];
        String second = strArray[1];
        return first + " " + second;
    }
}
