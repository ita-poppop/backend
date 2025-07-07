package com.example.poppop.domain.comment.service;

import com.example.poppop.domain.comment.dto.response.CommentLikeResponse;
import com.example.poppop.domain.member.entity.CustomOAuth2User;
import com.example.poppop.global.auth.model.PopPopOAuth2User;

public interface CommentLikeService {

    CommentLikeResponse toggle(Long commentId, PopPopOAuth2User oauth2User);
}

