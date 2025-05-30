package com.example.poppop.domain.popup.dto;

import com.example.poppop.domain.popup.entity.Popup;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

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
        String startDate = extractStartDate(date);
        String endDate = extractEndDate(date);

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

    private String extractStartDate(String date) {
        try {
            if(date!=null && !date.isEmpty()){
                String[] splitDate = date.split("~");
                String startDate = splitDate[0].trim();
                return startDate;
            }
        }catch (Exception e){
            log.error("날짜 분리가 잘못되었습니다(startDate)");
        }
        return null;
    }

    private String extractEndDate(String date) {
        try {
            if(date!=null && !date.isEmpty()){
                String[] splitDate = date.split("~");
                String endDate = splitDate[1].trim();
                return endDate;
            }
        }catch (Exception e){
            log.error("날짜 분리가 잘못되었습니다(endDate)");
        }
        return null;
    }

    public static String removeLocationNbsp(String location) {
        return location == null ? null :
                location.replace("복사", "")
                        .replace("&nbsp;", "")
                        .replaceAll("\\s+", " ")
                        .trim();
    }
}
