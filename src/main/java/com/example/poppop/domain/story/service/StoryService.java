package com.example.poppop.domain.story.service;

import com.example.poppop.domain.member.entity.CustomOAuth2User;
import com.example.poppop.domain.story.dto.request.StoryCreateRequest;
import com.example.poppop.domain.story.dto.response.PopupStoryResponse;
import com.example.poppop.domain.story.dto.response.StoryDetailResponse;
import com.example.poppop.domain.story.dto.response.StorySummaryResponse;

import java.util.List;

public interface StoryService {

    void create(Long popupId, StoryCreateRequest dto, CustomOAuth2User oauth2User);
    List<StorySummaryResponse> findAllStory(int page, int size, CustomOAuth2User oauth2User);
    List<PopupStoryResponse> findByPopup(Long popupId, int page, int size, CustomOAuth2User oauth2User);
    StoryDetailResponse findOneStory(Long popupId, Long storyId, CustomOAuth2User oauth2User);
    void delete(Long popupId, Long storyId, CustomOAuth2User oauth2User);
}
