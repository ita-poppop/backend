package com.example.poppop.domain.review.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record ReviewCreateRequest(
        String content,
        @NotEmpty List<@NotBlank String> imageUrls
) {}
