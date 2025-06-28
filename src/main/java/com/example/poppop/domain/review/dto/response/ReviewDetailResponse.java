package com.example.poppop.domain.review.dto.response;

import com.example.poppop.domain.review.entity.Review;

import java.time.LocalDateTime;
import java.util.List;

public record ReviewDetailResponse(
        Long reviewId,
        String content,
        List<String> imageUrls,
        String writerName,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        long likeCount,
        long commentCount
) {
    public static ReviewDetailResponse from(
            Review review,
            long likeCount,
            long rootCommentCount) {

        return new ReviewDetailResponse(
                review.getId(),
                review.getContent(),
                review.getImages().stream()
                        .map(img -> img.getUrl())
                        .toList(),
                review.getMember().getUserName(),
                review.getCreatedAt(),
                review.getUpdatedAt(),
                likeCount,
                rootCommentCount
        );
    }
}
