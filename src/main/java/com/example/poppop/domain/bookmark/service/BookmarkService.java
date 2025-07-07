package com.example.poppop.domain.bookmark.service;

import com.example.poppop.domain.bookmark.dto.response.PopupBookmarkResponse;
import com.example.poppop.global.auth.model.PopPopOAuth2User;

import java.util.List;

public interface BookmarkService {

    List<PopupBookmarkResponse> findAllBookmarks(Long memberId);
    void toggleBookmark(Long popupId, Long memberId);
    void deleteBookmark(Long popupId, Long memberId);
}
