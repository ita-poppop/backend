package com.example.poppop.domain.review.controller;

import com.example.poppop.domain.member.entity.CustomOAuth2User;
import com.example.poppop.domain.review.dto.request.ReviewCreateRequest;
import com.example.poppop.domain.review.dto.request.ReviewUpdateRequest;
import com.example.poppop.domain.review.dto.response.ReviewResponse;
import com.example.poppop.domain.review.service.ReviewService;
import com.example.poppop.domain.review.swagger.DeleteReview;
import com.example.poppop.domain.review.swagger.GetPopupReviews;
import com.example.poppop.domain.review.swagger.PostReview;
import com.example.poppop.domain.review.swagger.UpdateReview;
import com.example.poppop.global.common.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/popups/{popupId}/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostReview
    @PostMapping
    public ApiResponse<Void> createReview(
            @PathVariable Long popupId,
            @RequestBody @Valid ReviewCreateRequest request,
            @AuthenticationPrincipal CustomOAuth2User oauth2User) {

        reviewService.create(popupId, request, oauth2User);
        return ApiResponse.successMessage("리뷰가 등록되었습니다.");
    }

    @GetPopupReviews
    @GetMapping
    public ApiResponse<List<ReviewResponse>> getReviews(
            @PathVariable Long popupId,
            @RequestParam @Valid Integer page,
            @RequestParam @Valid Integer size) {

        return ApiResponse.success(reviewService.findAllByPopup(popupId, page, size));
    }

    @UpdateReview
    @PostMapping("/{reviewId}/patch")
    public ApiResponse<Void> updateReview(
            @PathVariable Long reviewId,
            @RequestBody @Valid ReviewUpdateRequest request,
            @AuthenticationPrincipal CustomOAuth2User oauth2User) {

        reviewService.update(reviewId, request, oauth2User);
        return ApiResponse.successMessage("리뷰가 수정되었습니다.");
    }

    @DeleteReview
    @PostMapping("/{reviewId}/delete")
    public ApiResponse<Void> deleteReview(
            @PathVariable Long reviewId,
            @AuthenticationPrincipal CustomOAuth2User oauth2User) {

        reviewService.delete(reviewId, oauth2User);
        return ApiResponse.successMessage("리뷰가 삭제되었습니다.");
    }
}

