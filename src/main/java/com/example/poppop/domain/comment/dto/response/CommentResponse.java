package com.example.poppop.domain.comment.dto.response;

import com.example.poppop.domain.comment.entity.Comment;

import java.time.LocalDateTime;
import java.util.List;

public record CommentResponse(
        Long commentId,
        String content,
        String writerName,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        List<CommentResponse> children   // 재귀
) {
    public static CommentResponse from(Comment comment) {
        return new CommentResponse(
                comment.getId(),
                comment.getContent(),
                comment.getMember().getUserName(),
                comment.getCreatedAt(),
                comment.getUpdatedAt(),
                comment.getChildren().stream()
                        .filter(child -> !child.getIsDeleted())
                        .map(CommentResponse::from)
                        .toList()
        );
    }
}
