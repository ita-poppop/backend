package com.example.poppop.domain.mypage.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.web.multipart.MultipartFile;

public record ProfileUpdateRequest(

        MultipartFile profileImage,
        @Size(max = 30) String userName
) {}
