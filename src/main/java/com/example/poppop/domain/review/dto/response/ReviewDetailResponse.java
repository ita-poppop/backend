package com.example.poppop.domain.review.dto.response;

import com.example.poppop.domain.review.entity.Review;

import java.time.LocalDateTime;
import java.util.List;

public record ReviewDetailResponse(
        Long reviewId,
        String content,
        List<String> imageUrls,
        String writerName,
        String writerProfileUrl,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        long likeCount,
        long commentCount,
        boolean likedByUser
) {
    public static ReviewDetailResponse from(
            Review review,
            long likeCount,
            long rootCommentCount,
            boolean likedByUser
    ) {

        return new ReviewDetailResponse(
                review.getId(),
                review.getContent(),
                review.getImages().stream()
                        .map(img -> img.getUrl())
                        .toList(),
                review.getMember().getUserName(),
                review.getMember().getProfileUrl(),
                review.getCreatedAt(),
                review.getUpdatedAt(),
                likeCount,
                rootCommentCount,
                likedByUser
        );
    }
}
