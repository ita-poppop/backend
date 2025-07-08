package com.example.poppop.domain.mypage.dto.request;

import jakarta.validation.constraints.Size;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

public record ProfileUpdateRequest(

        @RequestParam(value = "profileImage", required = false) MultipartFile profileImage,
        @Size(max = 30) String userName
) {}
