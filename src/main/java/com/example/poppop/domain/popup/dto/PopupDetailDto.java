package com.example.poppop.domain.popup.dto;

import com.example.poppop.domain.popup.entity.Popup;
import lombok.Builder;
import lombok.Getter;

@Getter
public class PopupDetailDto {
    private Long id;
    private String title;
    private String imageUrl;
    private String location;
    private String date; //startDate랑 endDate조합해서 만들어주기
    private String comment;
    private String detail;

    @Builder
    public PopupDetailDto(Long id, String title, String imageUrl, String location, String date, String comment, String detail) {
        this.id = id;
        this.title = title;
        this.imageUrl = imageUrl;
        this.location = location;
        this.date = date;
        this.comment = comment;
        this.detail = detail;
    }

    public static PopupDetailDto from(Popup popup) {
        return PopupDetailDto.builder()
                .id(popup.getId())
                .title(popup.getTitle())
                .imageUrl(popup.getImage())
                .location(popup.getLocation())
                .date(popup.getStartDate() + " ~ " + popup.getEndDate())
                .comment(popup.getComment())
                .detail(popup.getDetail())
                .build();
    }
}
