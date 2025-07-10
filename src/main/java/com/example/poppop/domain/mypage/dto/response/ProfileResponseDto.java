package com.example.poppop.domain.mypage.dto.response;

import java.time.LocalDate;
import java.util.List;

public record ProfileResponseDto(
        String profileUrl,
        String userName,
        long reviewCount,       // 내가 쓴 리뷰 총 개수
        long totalLikeCount,    // 내가 쓴 리뷰들의 좋아요 합계
        List<ProfileReviewDto> reviews
) {
    public record ProfileReviewDto(
            Long reviewId,
            Long popupId,
            String reviewContent,
            List<String> reviewImageUrls,
            String popupImageUrl,  // 팝업 대표 이미지 (리뷰와 연결된 팝업의 이미지)
            String popupTitle,
            LocalDate startDate,
            LocalDate endDate
    ) {}

    public static ProfileResponseDto of(
            String profileUrl,
            String userName,
            long reviewCount,
            long totalLikeCount,
            List<ProfileReviewDto> reviews
    ) {
        return new ProfileResponseDto(
                profileUrl, userName,
                reviewCount, totalLikeCount,
                reviews
        );
    }
}
