package com.example.poppop.domain.mypage.controller;

import com.example.poppop.domain.mypage.dto.request.ProfileUpdateRequest;
import com.example.poppop.domain.mypage.dto.response.ProfileResponseDto;
import com.example.poppop.domain.mypage.service.ProfileService;
import com.example.poppop.domain.story.dto.request.StoryCreateRequest;
import com.example.poppop.global.auth.model.PopPopOAuth2User;
import com.example.poppop.global.common.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/profile")
@RequiredArgsConstructor
@Validated
public class ProfileController {

    private final ProfileService profileService;

    @GetMapping
    public ApiResponse<ProfileResponseDto> getMyProfile(
            @AuthenticationPrincipal PopPopOAuth2User user
    ) {
        ProfileResponseDto dto = profileService.getMyProfile(user);
        return ApiResponse.success(dto);
    }

    @Operation(
            summary = "프로필 수정",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(
                            mediaType = MediaType.MULTIPART_FORM_DATA_VALUE,
                            schema = @Schema(implementation = ProfileUpdateRequest.class)
                    )
            )
    )
    @PostMapping(
            path = "/update",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ApiResponse<Void> updateMyProfile(
            @AuthenticationPrincipal PopPopOAuth2User user,
            @ModelAttribute @Valid ProfileUpdateRequest request
    ) {
        profileService.updateMyProfile(user, request);
        return ApiResponse.successMessage("프로필이 수정되었습니다.");
    }
}
