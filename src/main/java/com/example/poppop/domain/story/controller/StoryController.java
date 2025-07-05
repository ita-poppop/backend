package com.example.poppop.domain.story.controller;

import com.example.poppop.domain.member.entity.CustomOAuth2User;
import com.example.poppop.domain.story.dto.request.StoryCreateRequest;
import com.example.poppop.domain.story.dto.response.PopupStoryResponse;
import com.example.poppop.domain.story.dto.response.StoryDetailResponse;
import com.example.poppop.domain.story.service.StoryService;
import com.example.poppop.global.common.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/popups/{popupId}/stories")
@RequiredArgsConstructor
public class StoryController {
    private final StoryService storyService;

    @PostMapping
    public ApiResponse<Void> createStory(
            @PathVariable Long popupId,
            @RequestBody @Valid StoryCreateRequest request,
            @AuthenticationPrincipal CustomOAuth2User oauth2User) {

        storyService.create(popupId, request, oauth2User);
        return ApiResponse.successMessage("스토리가 등록되었습니다.");
    }

    @GetMapping
    public ApiResponse<Page<PopupStoryResponse>> getPopupStories(
            @PathVariable Long popupId,
            @RequestParam @Valid Integer page,
            @RequestParam @Valid Integer size,
            @AuthenticationPrincipal CustomOAuth2User oauth2User) {

        return ApiResponse.success(
                storyService.findByPopup(popupId, page, size, oauth2User)
        );
    }

    @GetMapping("/{storyId}")
    public ApiResponse<StoryDetailResponse> getStoryDetail(
            @PathVariable Long popupId,
            @PathVariable Long storyId,
            @AuthenticationPrincipal CustomOAuth2User oauth2User) {

        return ApiResponse.success(
                storyService.findOneStory(popupId, storyId, oauth2User)
        );
    }
}
