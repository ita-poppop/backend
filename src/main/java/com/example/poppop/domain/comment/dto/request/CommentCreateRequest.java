package com.example.poppop.domain.comment.dto.request;

public record CommentCreateRequest(
        String content,
        Long parentId // null → 1차 댓글, 값 있으면 대댓글
) {}
