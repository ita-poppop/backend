package com.example.poppop.domain.story.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

public record StoryCreateRequest(
//        @NotBlank String photoUrl,
        @Schema(type = "string", format = "binary", description = "업로드할 이미지 파일")
        @NotNull MultipartFile photo,
        @NotNull @Min(0) Integer estimatedWaitTime,
        @NotNull @Min(0) Integer estimatedWaitCount
) {}
