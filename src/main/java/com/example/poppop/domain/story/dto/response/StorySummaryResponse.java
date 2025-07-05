package com.example.poppop.domain.story.dto.response;

import java.time.LocalDateTime;

public record StorySummaryResponse(
        Long   storyId,
        String photoUrl,
        int    estimatedWaitTime,
        int    estimatedWaitCount,
        String popupTitle,
        String popupLocation,
        String writerName,
        String writerProfileUrl,
        boolean isRead,
        LocalDateTime createdAt
) {}
