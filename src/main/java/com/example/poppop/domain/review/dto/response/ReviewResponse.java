package com.example.poppop.domain.review.dto.response;

import com.example.poppop.domain.review.entity.Review;
import com.example.poppop.domain.review.entity.ReviewImage;

import java.time.LocalDateTime;
import java.util.List;

public record ReviewResponse(
        Long reviewId,
        String content,
        List<String> imageUrls,
        String writerName,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static ReviewResponse from(Review review) {
        return new ReviewResponse(
                review.getId(),
                review.getContent(),
                review.getImages().stream()
                        .map(ReviewImage::getUrl)
                        .toList(),
                review.getMember().getUserName(),
                review.getCreatedAt(),
                review.getUpdatedAt()
        );
    }
}
