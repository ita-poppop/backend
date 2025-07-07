package com.example.poppop.domain.comment.service;

import com.example.poppop.domain.comment.dto.request.CommentCreateRequest;
import com.example.poppop.domain.comment.dto.request.CommentUpdateRequest;
import com.example.poppop.domain.comment.dto.response.CommentListResponse;
import com.example.poppop.domain.comment.dto.response.CommentResponse;
import com.example.poppop.domain.member.entity.CustomOAuth2User;
import com.example.poppop.global.auth.model.PopPopOAuth2User;

import java.util.List;

public interface CommentService {

    void create(Long reviewId, CommentCreateRequest dto, PopPopOAuth2User oauth2user);
    List<CommentListResponse> findAllByReview(Long reviewId, int page, int size);
    CommentResponse findOneComment(Long reviewId, Long commentId);
    List<CommentResponse> findReplies(Long commentId, int page, int size);
    void update(Long commentId, CommentUpdateRequest dto, PopPopOAuth2User oauth2user);
    void delete(Long commentId, PopPopOAuth2User oauth2user);

}
