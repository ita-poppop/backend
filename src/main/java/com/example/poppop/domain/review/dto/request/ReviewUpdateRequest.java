package com.example.poppop.domain.review.dto.request;

import jakarta.validation.constraints.NotBlank;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public record ReviewUpdateRequest(
//        @NotBlank String content
        String content,
        List<MultipartFile> images
) {}
