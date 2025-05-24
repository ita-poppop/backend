package com.example.poppop.domain.review.service;

import com.example.poppop.domain.member.entity.Member;
import com.example.poppop.domain.member.repository.MemberRepository;
import com.example.poppop.domain.popup.entity.Popup;
import com.example.poppop.domain.popup.repository.PopupRepository;
import com.example.poppop.domain.review.dto.request.ReviewCreateRequest;
import com.example.poppop.domain.review.dto.request.ReviewUpdateRequest;
import com.example.poppop.domain.review.dto.response.ReviewResponse;
import com.example.poppop.domain.review.entity.Review;
import com.example.poppop.domain.review.entity.ReviewImage;
import com.example.poppop.domain.review.error.ReviewErrorCode;
import com.example.poppop.domain.review.repository.ReviewRepository;
import com.example.poppop.global.error.exception.CustomException;
import lombok.RequiredArgsConstructor;
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

    @Override
    @Transactional
    public ReviewResponse create(Long popupId, ReviewCreateRequest dto) {

        if (dto.imageUrls() == null || dto.imageUrls().isEmpty()) {
            throw new CustomException(ReviewErrorCode.IMAGE_REQUIRED);
        }

        Member member = memberRepository.findById(dto.memberId())
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
        return ReviewResponse.from(review);
    }

    @Override
    public List<ReviewResponse> findAllByPopup(Long popupId) {

        Popup popup = popupRepository.findById(popupId)
                .orElseThrow(() -> new CustomException(ReviewErrorCode.POPUP_NOT_FOUND));

        return reviewRepository.findByPopUpAndIsDeletedFalseOrderByCreatedAtDesc(popup)
                .stream()
                .map(ReviewResponse::from)
                .toList();
    }

    @Override
    @Transactional
    public ReviewResponse update(Long reviewId, ReviewUpdateRequest dto) {

        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new CustomException(ReviewErrorCode.REVIEW_NOT_FOUND));

        review.updateContent(dto.content());
        return ReviewResponse.from(review);
    }

    @Override
    @Transactional
    public void delete(Long reviewId) {

        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new CustomException(ReviewErrorCode.REVIEW_NOT_FOUND));

        review.softDelete();
    }
}
