package com.example.poppop.domain.story.dto.response;

import java.time.LocalDateTime;

public record StoryDetailResponse(
        Long   storyId,
        String photoUrl,
        String writerName,
        String writerProfileUrl,
        String popupTitle,
        LocalDateTime createdAt
) {}
