package com.example.poppop.domain.bookmark.controller;

import com.example.poppop.domain.bookmark.dto.response.PopupBookmarkResponse;
import com.example.poppop.domain.bookmark.service.BookmarkService;
import com.example.poppop.domain.member.service.MemberService;
import com.example.poppop.global.auth.model.PopPopOAuth2User;
import com.example.poppop.global.common.ApiResponse;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Validated
public class BookmarkController {

    private final BookmarkService bookmarkService;

    @GetMapping("/bookmarks")
    public ApiResponse<List<PopupBookmarkResponse>> findBookmarks(
            @AuthenticationPrincipal PopPopOAuth2User oAuth2User
    ) {

        List<PopupBookmarkResponse> list = bookmarkService.findAllBookmarks(oAuth2User);

        return ApiResponse.success(list);
    }

    @PostMapping("/popups/{popupId}/bookmark")
    public ApiResponse<Void> addOrCancelBookmark(
            @PathVariable @Positive Long popupId,
            @AuthenticationPrincipal PopPopOAuth2User oAuth2User
    ) {

        bookmarkService.toggleBookmark(popupId, oAuth2User);
        return ApiResponse.successMessage("즐겨찾기 상태가 변경되었습니다.");
    }

    @PostMapping("/popups/{popupId}/bookmark/delete")
    public ApiResponse<Void> CancelBookmark(
            @PathVariable @Positive Long popupId,
            @AuthenticationPrincipal PopPopOAuth2User oAuth2User
    ) {

        bookmarkService.deleteBookmark(popupId, oAuth2User);
        return ApiResponse.successMessage("즐겨찾기가 삭제되었습니다.");
    }
}
