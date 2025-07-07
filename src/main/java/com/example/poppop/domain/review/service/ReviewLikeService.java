package com.example.poppop.domain.review.service;

import com.example.poppop.domain.member.entity.CustomOAuth2User;
import com.example.poppop.domain.review.dto.response.ReviewLikeResponse;
import com.example.poppop.global.auth.model.PopPopOAuth2User;

public interface ReviewLikeService {

    ReviewLikeResponse toggle(Long reviewId, PopPopOAuth2User oauth2User);
}
