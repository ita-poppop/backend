package com.example.poppop.domain.story.dto.response;

import java.time.LocalDateTime;

public record PopupStoryResponse(
        Long   storyId,
        String photoUrl,
        String writerName,
        String profileUrl,
        boolean isRead,
        LocalDateTime createdAt
) {}
