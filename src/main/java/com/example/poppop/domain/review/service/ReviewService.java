package com.example.poppop.domain.review.service;

import com.example.poppop.domain.member.entity.CustomOAuth2User;
import com.example.poppop.domain.review.dto.request.ReviewCreateRequest;
import com.example.poppop.domain.review.dto.request.ReviewUpdateRequest;
import com.example.poppop.domain.review.dto.response.ReviewDetailResponse;
import com.example.poppop.domain.review.dto.response.ReviewResponse;
import com.example.poppop.global.auth.model.PopPopOAuth2User;

import java.util.List;

public interface ReviewService {

    void create(Long popupId, ReviewCreateRequest dto, PopPopOAuth2User oauth2User);
    List<ReviewResponse> findAllByPopup(Long popupId, int page, int size);
    void update(Long reviewId, ReviewUpdateRequest dto, PopPopOAuth2User oauth2User);
    void delete(Long reviewId, PopPopOAuth2User oauth2User);
    ReviewDetailResponse findOneReview(Long popupId, Long reviewId);
}

