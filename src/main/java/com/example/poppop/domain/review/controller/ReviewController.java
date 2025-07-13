package com.example.poppop.domain.review.controller;

import com.example.poppop.domain.member.entity.CustomOAuth2User;
import com.example.poppop.domain.review.dto.request.ReviewCreateRequest;
import com.example.poppop.domain.review.dto.request.ReviewUpdateRequest;
import com.example.poppop.domain.review.dto.response.ReviewDetailResponse;
import com.example.poppop.domain.review.dto.response.ReviewResponse;
import com.example.poppop.domain.review.service.ReviewService;
import com.example.poppop.domain.review.swagger.*;
import com.example.poppop.global.auth.model.PopPopOAuth2User;
import com.example.poppop.global.common.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/popups/{popupId}/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostReview
    @PostMapping(
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ApiResponse<Void> createReview(
            @PathVariable Long popupId,
            @ModelAttribute @Valid ReviewCreateRequest request,
            @AuthenticationPrincipal PopPopOAuth2User oauth2User) {

        reviewService.create(popupId, request, oauth2User);
        return ApiResponse.successMessage("리뷰가 등록되었습니다.");
    }

    @GetPopupReviews
    @GetMapping
    public ApiResponse<List<ReviewResponse>> getReviews(
            @PathVariable Long popupId,
            @RequestParam @Valid Integer page,
            @RequestParam @Valid Integer size,
            @AuthenticationPrincipal PopPopOAuth2User oauth2User) {

        return ApiResponse.success(reviewService.findAllByPopup(popupId, page, size, oauth2User));
    }

    @GetSingleReview
    @GetMapping("/{reviewId}")
    public ApiResponse<ReviewDetailResponse> getReview(
            @PathVariable Long popupId,
            @PathVariable Long reviewId,
            @AuthenticationPrincipal PopPopOAuth2User oauth2User) {

        return ApiResponse.success(
                reviewService.findOneReview(popupId, reviewId, oauth2User)
        );
    }

//    @UpdateReview
//    @PostMapping("/{reviewId}/patch")
//    public ApiResponse<Void> updateReview(
//            @PathVariable Long reviewId,
//            @RequestBody @Valid ReviewUpdateRequest request,
//            @AuthenticationPrincipal PopPopOAuth2User oauth2User) {
//
//        reviewService.update(reviewId, request, oauth2User);
//        return ApiResponse.successMessage("리뷰가 수정되었습니다.");
//    }

    @UpdateReview
    @PostMapping(
            path     = "/{reviewId}/patch",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ApiResponse<Void> updateReview(
            @PathVariable Long reviewId,
            @ModelAttribute @Valid ReviewUpdateRequest request,
            @AuthenticationPrincipal PopPopOAuth2User user
    ) {
        reviewService.update(reviewId, request, user);
        return ApiResponse.successMessage("리뷰가 수정되었습니다.");
    }

    @DeleteReview
    @PostMapping("/{reviewId}/delete")
    public ApiResponse<Void> deleteReview(
            @PathVariable Long reviewId,
            @AuthenticationPrincipal PopPopOAuth2User oauth2User) {

        reviewService.delete(reviewId, oauth2User);
        return ApiResponse.successMessage("리뷰가 삭제되었습니다.");
    }
}

