package com.example.poppop.domain.comment.service;

import com.example.poppop.domain.comment.dto.request.CommentCreateRequest;
import com.example.poppop.domain.comment.dto.request.CommentUpdateRequest;
import com.example.poppop.domain.comment.dto.response.CommentListResponse;
import com.example.poppop.domain.comment.dto.response.CommentResponse;
import com.example.poppop.domain.comment.entity.Comment;
import com.example.poppop.domain.comment.error.CommentErrorCode;
import com.example.poppop.domain.comment.repository.CommentRepository;
import com.example.poppop.domain.member.entity.CustomOAuth2User;
import com.example.poppop.domain.member.entity.Member;
import com.example.poppop.domain.member.repository.MemberRepository;
import com.example.poppop.domain.review.entity.Review;
import com.example.poppop.domain.review.repository.ReviewRepository;
import com.example.poppop.global.error.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final ReviewRepository reviewRepository;
    private final MemberRepository memberRepository;

    @Override
    @Transactional
    public void create(Long reviewId, CommentCreateRequest dto, CustomOAuth2User oauth2user) {

        Member member = memberRepository.findById(oauth2user.getId())
                .orElseThrow(() -> new CustomException(CommentErrorCode.MEMBER_NOT_FOUND));

        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new CustomException(CommentErrorCode.REVIEW_NOT_FOUND));

        Comment parent = null;
        if (dto.parentId() != null) {
            parent = commentRepository.findById(dto.parentId())
                    .orElseThrow(() -> new CustomException(CommentErrorCode.COMMENT_NOT_FOUND));
            if (!parent.getReview().equals(review)) // 부모-자식 리뷰 불일치
                throw new CustomException(CommentErrorCode.INVALID_PARENT);
        }

        Comment comment = Comment.builder()
                .content(dto.content())
                .member(member)
                .review(review)
                .parent(parent)
                .build();
        if (parent != null) parent.addChild(comment);

        commentRepository.save(comment);
    }

    @Override
    public List<CommentListResponse> findAllByReview(Long reviewId) {

        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new CustomException(CommentErrorCode.REVIEW_NOT_FOUND));

        List<Comment> roots = commentRepository.findRootComments(review);

        return roots.stream()
                .map(c -> new CommentListResponse(
                        c.getId(),
                        c.getContent(),
                        c.getMember().getUserName(),
                        c.getCreatedAt(),
                        c.getUpdatedAt(),
                        // 대댓글 개수만 조회
                        (int) c.getChildren().stream()
                                .filter(child -> !child.getIsDeleted())
                                .count()
                ))
                .toList();
    }

    @Override
    public CommentResponse findOneComment(Long reviewId, Long commentId) {
        // 리뷰 존재 검증
        reviewRepository.findById(reviewId)
                .orElseThrow(() -> new CustomException(CommentErrorCode.REVIEW_NOT_FOUND));

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CustomException(CommentErrorCode.COMMENT_NOT_FOUND));

        return CommentResponse.from(comment);
    }

    @Override
    @Transactional
    public void update(Long commentId, CommentUpdateRequest dto, CustomOAuth2User oauth2user) {

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CustomException(CommentErrorCode.COMMENT_NOT_FOUND));

        if (!comment.getMember().getId().equals(oauth2user.getId()))
            throw new CustomException(CommentErrorCode.INVALID_PERMISSION);

        comment.updateContent(dto.content());
    }

    @Override
    @Transactional
    public void delete(Long commentId, CustomOAuth2User oauth2user) {

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CustomException(CommentErrorCode.COMMENT_NOT_FOUND));

        if (!comment.getMember().getId().equals(oauth2user.getId()))
            throw new CustomException(CommentErrorCode.INVALID_PERMISSION);

        comment.softDelete();
    }
}

