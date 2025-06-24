package com.example.poppop.domain.review.dto.response;

public record ReviewLikeResponse(
        boolean liked,
        long likeCount
) {}
