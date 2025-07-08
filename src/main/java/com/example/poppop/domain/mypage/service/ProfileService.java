package com.example.poppop.domain.mypage.service;

import com.example.poppop.domain.mypage.dto.request.ProfileUpdateRequest;
import com.example.poppop.domain.mypage.dto.response.ProfileResponseDto;
import com.example.poppop.global.auth.model.PopPopOAuth2User;

public interface ProfileService {

    ProfileResponseDto getMyProfile(PopPopOAuth2User user);
    void updateMyProfile(PopPopOAuth2User user, ProfileUpdateRequest request);
}
