package com.example.poppop.domain.comment.service;

import com.example.poppop.domain.comment.dto.response.CommentLikeResponse;
import com.example.poppop.domain.member.entity.CustomOAuth2User;

public interface CommentLikeService {

    CommentLikeResponse toggle(Long commentId, CustomOAuth2User oauth2User);
}

