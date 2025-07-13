package com.example.poppop.domain.review.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public record ReviewCreateRequest(
        String content,
        @NotEmpty(message = "리뷰에는 최소 1장의 이미지가 필요합니다.")
        List<MultipartFile> images
) {}
