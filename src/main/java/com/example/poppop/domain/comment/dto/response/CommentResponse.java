package com.example.poppop.domain.comment.dto.response;

import com.example.poppop.domain.comment.entity.Comment;

import java.time.LocalDateTime;
import java.util.List;

public record CommentResponse(
        Long commentId,
        String content,
        String writerName,
        String writerProfileUrl,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        List<CommentResponse> children   // 재귀
) {
    public static CommentResponse from(com.example.poppop.domain.comment.entity.Comment c) {
        List<CommentResponse> kids = c.getChildren().stream()
                .filter(ch -> !ch.getIsDeleted())
                .map(CommentResponse::from)
                .toList();

        return new CommentResponse(
                c.getId(),
                c.getContent(),
                c.getMember().getUserName(),
                c.getMember().getProfileUrl(),
                c.getCreatedAt(),
                c.getUpdatedAt(),
                kids
        );
    }
}
