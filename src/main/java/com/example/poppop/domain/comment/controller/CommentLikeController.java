package com.example.poppop.domain.comment.controller;

import com.example.poppop.domain.comment.dto.response.CommentLikeResponse;
import com.example.poppop.domain.comment.service.CommentLikeService;
import com.example.poppop.domain.comment.swagger.ToggleCommentLike;
import com.example.poppop.domain.member.entity.CustomOAuth2User;
import com.example.poppop.global.common.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/comments/{commentId}/likes")
@RequiredArgsConstructor
public class CommentLikeController {

    private final CommentLikeService likeService;

    @ToggleCommentLike
    @PostMapping("/toggle")
    public ApiResponse<CommentLikeResponse> toggle(
            @PathVariable Long commentId,
            @AuthenticationPrincipal CustomOAuth2User oauth2User) {

        CommentLikeResponse res = likeService.toggle(commentId, oauth2User);
        return ApiResponse.success(res,
                res.liked() ? "좋아요!" : "좋아요 취소");
    }
}
