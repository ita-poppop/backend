package com.example.poppop.domain.comment.dto.response;

import java.time.LocalDateTime;

public record CommentListResponse(
        Long commentId,
        String content,
        String writerName,
        String writerProfileUrl,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        int replyCount
) {}
