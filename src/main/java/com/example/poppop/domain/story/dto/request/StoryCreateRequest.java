package com.example.poppop.domain.story.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record StoryCreateRequest(
        @NotBlank String photoUrl,
        @NotNull @Min(0) Integer estimatedWaitTime,
        @NotNull @Min(0) Integer estimatedWaitCount
) {}
