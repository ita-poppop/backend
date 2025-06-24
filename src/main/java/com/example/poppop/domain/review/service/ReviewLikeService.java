package com.example.poppop.domain.review.service;

import com.example.poppop.domain.member.entity.CustomOAuth2User;
import com.example.poppop.domain.review.dto.response.ReviewLikeResponse;

public interface ReviewLikeService {

    ReviewLikeResponse toggle(Long reviewId, CustomOAuth2User oauth2User);
}
