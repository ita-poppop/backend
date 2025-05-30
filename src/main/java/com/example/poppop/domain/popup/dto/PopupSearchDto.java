package com.example.poppop.domain.popup.dto;

import com.example.poppop.domain.popup.entity.Popup;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Slf4j
public class PopupSearchDto {
    private Long id;
    private String image;
    private String title;
    private String location;

    @Builder
    public PopupSearchDto(Long id, String imageUrl, String title, String location) {
        this.id = id;
        this.image = imageUrl;
        this.title = title;
        this.location = location;
    }

    public static PopupSearchDto from(Popup popup) {
       return PopupSearchDto.builder()
                .id(popup.getId())
                .imageUrl(popup.getImage())
                .title(popup.getTitle())
                .location(extractLocation(popup.getLocation()))
                .build();
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
