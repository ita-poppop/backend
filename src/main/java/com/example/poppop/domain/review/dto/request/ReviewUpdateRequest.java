package com.example.poppop.domain.review.dto.request;

import jakarta.validation.constraints.NotBlank;

public record ReviewUpdateRequest(
        @NotBlank String content
) {}
