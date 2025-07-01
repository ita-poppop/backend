package com.example.poppop.domain.review.service;

import com.example.poppop.domain.comment.repository.CommentRepository;
import com.example.poppop.domain.member.entity.CustomOAuth2User;
import com.example.poppop.domain.member.entity.Member;
import com.example.poppop.domain.member.repository.MemberRepository;
import com.example.poppop.domain.popup.entity.Popup;
import com.example.poppop.domain.popup.repository.PopupRepository;
import com.example.poppop.domain.review.dto.request.ReviewCreateRequest;
import com.example.poppop.domain.review.dto.request.ReviewUpdateRequest;
import com.example.poppop.domain.review.dto.response.ReviewDetailResponse;
import com.example.poppop.domain.review.dto.response.ReviewResponse;
import com.example.poppop.domain.review.entity.Review;
import com.example.poppop.domain.review.entity.ReviewImage;
import com.example.poppop.domain.review.error.ReviewErrorCode;
import com.example.poppop.domain.review.repository.ReviewLikeRepository;
import com.example.poppop.domain.review.repository.ReviewRepository;
import com.example.poppop.global.error.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final MemberRepository memberRepository;
    private final PopupRepository popupRepository;
    private final ReviewLikeRepository reviewLikeRepository;
    private final CommentRepository commentRepository;

    @Override
    @Transactional
    public void create(Long popupId, ReviewCreateRequest dto, CustomOAuth2User oauth2User) {

        Long memberId = oauth2User.getId();

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ReviewErrorCode.MEMBER_NOT_FOUND));

        Popup popup = popupRepository.findById(popupId)
                .orElseThrow(() -> new CustomException(ReviewErrorCode.POPUP_NOT_FOUND));

        Review review = Review.builder()
                .content(dto.content())
                .member(member)
                .popup(popup)
                .build();

        dto.imageUrls().forEach(url ->
                review.addImage(ReviewImage.of(url, review)));

        reviewRepository.save(review);
    }

    @Override
    public List<ReviewResponse> findAllByPopup(Long popupId, int page, int size) {

        Popup popup = popupRepository.findById(popupId)
                .orElseThrow(() -> new CustomException(ReviewErrorCode.POPUP_NOT_FOUND));

        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "createdAt"));



        return reviewRepository
                .findByPopupAndIsDeletedFalseOrderByCreatedAtDesc(popup, pageable)
                .stream()
                .map(review -> {
                    long likeCnt = reviewLikeRepository
                            .countByReviewAndLikedTrue(review);
                    long commentCnt = commentRepository
                            .countByReviewAndParentIsNullAndIsDeletedFalse(review);
                    return ReviewResponse.from(review, likeCnt, commentCnt);
                })
                .toList();
    }

    @Override
    public ReviewDetailResponse findOneReview(Long popupId, Long reviewId) {
        Popup popup = popupRepository.findById(popupId)
                .orElseThrow(() -> new CustomException(ReviewErrorCode.POPUP_NOT_FOUND));

        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new CustomException(ReviewErrorCode.REVIEW_NOT_FOUND));

        // 리뷰가 그 팝업에 속하는지 검증(Optional)
        if (!review.getPopup().getId().equals(popup.getId())) {
            throw new CustomException(ReviewErrorCode.REVIEW_NOT_FOUND);
        }

        // 좋아요·댓글 카운트 집계
        long likeCount = reviewLikeRepository
                .countByReviewAndLikedTrue(review);
        long commentCount = commentRepository
                .countByReviewAndParentIsNullAndIsDeletedFalse(review);

        return ReviewDetailResponse.from(review, likeCount, commentCount);
    }

    @Override
    @Transactional
    public void update(Long reviewId, ReviewUpdateRequest dto, CustomOAuth2User oauth2User) {

        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new CustomException(ReviewErrorCode.REVIEW_NOT_FOUND));

        if (!review.getMember().getId().equals(oauth2User.getId())) {
            throw new CustomException(ReviewErrorCode.INVALID_PERMISSION);
        }

        review.updateContent(dto.content());
    }

    @Override
    @Transactional
    public void delete(Long reviewId, CustomOAuth2User oauth2User) {

        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new CustomException(ReviewErrorCode.REVIEW_NOT_FOUND));

        if (!review.getMember().getId().equals(oauth2User.getId())) {
            throw new CustomException(ReviewErrorCode.INVALID_PERMISSION);
        }

        review.softDelete();
    }
}
