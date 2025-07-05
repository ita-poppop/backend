package com.example.poppop.domain.story.controller;

import com.example.poppop.domain.member.entity.CustomOAuth2User;
import com.example.poppop.domain.story.dto.response.StorySummaryResponse;
import com.example.poppop.domain.story.service.StoryService;
import com.example.poppop.global.common.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/stories")
@RequiredArgsConstructor
public class StoryGlobalController {
    private final StoryService storyService;

    @GetMapping
    public ApiResponse<Page<StorySummaryResponse>> getAllStories(
            @RequestParam @Valid Integer page,
            @RequestParam @Valid Integer size,
            @AuthenticationPrincipal CustomOAuth2User oauth2User) {

        return ApiResponse.success(
                storyService.findAllStory(page, size, oauth2User)
        );
    }
}
