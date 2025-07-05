package com.example.poppop.domain.bookmark.service;

import com.example.poppop.domain.bookmark.dto.response.PopupBookmarkResponse;
import com.example.poppop.domain.member.entity.CustomOAuth2User;

import java.util.List;

public interface BookmarkService {

    List<PopupBookmarkResponse> findAllBookmarks(CustomOAuth2User oauth2User);
    void toggleBookmark(Long popupId, CustomOAuth2User oauth2User);
    void deleteBookmark(Long popupId, CustomOAuth2User oauth2User);
}
