package com.example.poppop.domain.review.controller;

import com.example.poppop.domain.member.entity.CustomOAuth2User;
import com.example.poppop.domain.review.dto.response.ReviewLikeResponse;
import com.example.poppop.domain.review.service.ReviewLikeService;
import com.example.poppop.domain.review.swagger.ToggleReviewLike;
import com.example.poppop.global.common.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/popups/{popupId}/reviews/{reviewId}/likes")
@RequiredArgsConstructor
public class ReviewLikeController {

    private final ReviewLikeService likeService;

    @ToggleReviewLike
    @PostMapping("/toggle")
    public ApiResponse<ReviewLikeResponse> toggleLike(
            @PathVariable Long reviewId,
            @AuthenticationPrincipal CustomOAuth2User oauth2User) {

        ReviewLikeResponse res = likeService.toggle(reviewId, oauth2User);
        return ApiResponse.success(res, res.liked() ? "좋아요!" : "좋아요 취소");
    }
}
