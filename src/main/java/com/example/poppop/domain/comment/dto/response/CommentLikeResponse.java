package com.example.poppop.domain.comment.dto.response;

public record CommentLikeResponse(
        boolean liked,
        long likeCount
) {}
