package com.example.poppop.domain.comment.service;

import com.example.poppop.domain.comment.dto.response.CommentLikeResponse;
import com.example.poppop.domain.comment.entity.Comment;
import com.example.poppop.domain.comment.entity.CommentLike;
import com.example.poppop.domain.comment.error.CommentLikeErrorCode;
import com.example.poppop.domain.comment.repository.CommentLikeRepository;
import com.example.poppop.domain.comment.repository.CommentRepository;
import com.example.poppop.domain.member.entity.CustomOAuth2User;
import com.example.poppop.domain.member.entity.Member;
import com.example.poppop.domain.member.repository.MemberRepository;
import com.example.poppop.global.error.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class CommentLikeServiceImpl implements CommentLikeService {

    private final CommentLikeRepository commentLikeRepository;
    private final CommentRepository commentRepository;
    private final MemberRepository memberRepository;

    @Override
    public CommentLikeResponse toggle(Long commentId, CustomOAuth2User oauth2User) {

        Member member = memberRepository.findById(oauth2User.getId())
                .orElseThrow(() -> new CustomException(CommentLikeErrorCode.MEMBER_NOT_FOUND));

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CustomException(CommentLikeErrorCode.COMMENT_NOT_FOUND));

        CommentLike like = commentLikeRepository.findByCommentAndMember(comment, member)
                .orElseGet(() -> commentLikeRepository.save(CommentLike.of(member, comment)));

        like.toggle();
        long cnt = commentLikeRepository.countByCommentAndLikedTrue(comment);

        return new CommentLikeResponse(like.getLiked(), cnt);
    }
}

