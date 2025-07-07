package com.example.poppop.domain.story.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

public record StoryCreateRequest(
//        @NotBlank String photoUrl,
        @NotNull MultipartFile photo,
        @NotNull @Min(0) Integer estimatedWaitTime,
        @NotNull @Min(0) Integer estimatedWaitCount
) {}
