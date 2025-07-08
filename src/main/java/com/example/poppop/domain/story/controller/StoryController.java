package com.example.poppop.domain.story.controller;

import com.example.poppop.domain.story.dto.request.StoryCreateRequest;
import com.example.poppop.domain.story.dto.response.PopupStoryResponse;
import com.example.poppop.domain.story.dto.response.StoryDetailResponse;
import com.example.poppop.domain.story.service.StoryService;
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

import java.util.List;

@RestController
@RequestMapping("/api/v1/popups/{popupId}/stories")
@RequiredArgsConstructor
@Validated
public class StoryController {
    private final StoryService storyService;

//    @PostMapping
//    public ApiResponse<Void> createStory(
//            @PathVariable Long popupId,
//            @RequestBody @Valid StoryCreateRequest request,
//            @AuthenticationPrincipal CustomOAuth2User oauth2User) {
//
//        storyService.create(popupId, request, oauth2User);
//        return ApiResponse.successMessage("스토리가 등록되었습니다.");
//    }


    @Operation(
            summary = "스토리 등록",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(
                            mediaType = MediaType.MULTIPART_FORM_DATA_VALUE,
                            schema = @Schema(implementation = StoryCreateRequest.class)
                    )
            )
    )
    @PostMapping(
            path = "/api/v1/popups/{popupId}/stories",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ApiResponse<Void> createStory(
            @PathVariable Long popupId,
            @ModelAttribute @Valid StoryCreateRequest request,
            @AuthenticationPrincipal PopPopOAuth2User oauth2User) {

        storyService.create(popupId, request, oauth2User);
        return ApiResponse.successMessage("스토리가 등록되었습니다.");
    }

    @GetMapping
    public ApiResponse<List<PopupStoryResponse>> getPopupStories(
            @PathVariable Long popupId,
            @RequestParam @Valid Integer page,
            @RequestParam @Valid Integer size,
            @AuthenticationPrincipal PopPopOAuth2User oauth2User) {

        return ApiResponse.success(
                storyService.findByPopup(popupId, page, size, oauth2User)
        );
    }

    @GetMapping("/{storyId}")
    public ApiResponse<StoryDetailResponse> getStoryDetail(
            @PathVariable Long popupId,
            @PathVariable Long storyId,
            @AuthenticationPrincipal PopPopOAuth2User oauth2User) {

        return ApiResponse.success(
                storyService.findOneStory(popupId, storyId, oauth2User)
        );
    }

    @PostMapping("/{storyId}/delete")
    public ApiResponse<Void> deleteStory(
            @PathVariable Long popupId,
            @PathVariable Long storyId,
            @AuthenticationPrincipal PopPopOAuth2User oauth2User) {

        storyService.delete(popupId, storyId, oauth2User);
        return ApiResponse.successMessage("스토리가 삭제되었습니다.");
    }
}
