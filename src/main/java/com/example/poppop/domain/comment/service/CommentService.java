package com.example.poppop.domain.comment.service;

import com.example.poppop.domain.comment.dto.request.CommentCreateRequest;
import com.example.poppop.domain.comment.dto.request.CommentUpdateRequest;
import com.example.poppop.domain.comment.dto.response.CommentResponse;
import com.example.poppop.domain.member.entity.CustomOAuth2User;

import java.util.List;

public interface CommentService {

    void create(Long reviewId, CommentCreateRequest dto, CustomOAuth2User oauth2user);
    List<CommentResponse> findByReview(Long reviewId);
    void update(Long commentId, CommentUpdateRequest dto, CustomOAuth2User oauth2user);
    void delete(Long commentId, CustomOAuth2User oauth2user);

}
