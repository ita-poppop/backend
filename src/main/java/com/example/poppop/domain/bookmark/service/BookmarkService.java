package com.example.poppop.domain.bookmark.service;

import com.example.poppop.domain.bookmark.dto.response.PopupBookmarkResponse;
import com.example.poppop.global.auth.model.PopPopOAuth2User;

import java.util.List;

public interface BookmarkService {

    List<PopupBookmarkResponse> findAllBookmarks(PopPopOAuth2User oAuth2User, int page, int size);
    void toggleBookmark(Long popupId, PopPopOAuth2User oAuth2User);
    void deleteBookmark(Long popupId, PopPopOAuth2User oAuth2User);
}
