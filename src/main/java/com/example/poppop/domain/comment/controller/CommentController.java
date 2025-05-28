package com.example.poppop.domain.comment.controller;

import com.example.poppop.domain.comment.dto.request.CommentCreateRequest;
import com.example.poppop.domain.comment.dto.request.CommentUpdateRequest;
import com.example.poppop.domain.comment.dto.response.CommentResponse;
import com.example.poppop.domain.comment.service.CommentService;
import com.example.poppop.domain.member.entity.CustomOAuth2User;
import com.example.poppop.global.common.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/popups/{popupId}/reviews/{reviewId}/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    public ApiResponse<Void> createComment(
            @PathVariable Long reviewId,
            @RequestBody @Valid CommentCreateRequest request,
            @AuthenticationPrincipal CustomOAuth2User oauth2user) {

        commentService.create(reviewId, request, oauth2user);
        return ApiResponse.successMessage("댓글이 등록되었습니다.");
    }

    @GetMapping
    public ApiResponse<List<CommentResponse>> getComments(@PathVariable Long reviewId) {
        return ApiResponse.success(commentService.findByReview(reviewId));
    }

    @PostMapping("/{commentId}/patch")
    public ApiResponse<Void> updateComment(
            @PathVariable Long commentId,
            @RequestBody @Valid CommentUpdateRequest request,
            @AuthenticationPrincipal CustomOAuth2User oauth2user) {

        commentService.update(commentId, request, oauth2user);
        return ApiResponse.successMessage("댓글이 수정되었습니다.");
    }

    @PostMapping("/{commentId/delete}")
    public ApiResponse<Void> deleteComment(
            @PathVariable Long commentId,
            @AuthenticationPrincipal CustomOAuth2User oauth2user) {

        commentService.delete(commentId, oauth2user);
        return ApiResponse.successMessage("댓글이 삭제되었습니다.");
    }
}

