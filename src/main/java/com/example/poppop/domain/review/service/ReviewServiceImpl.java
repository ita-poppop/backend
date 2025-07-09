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
import com.example.poppop.global.auth.model.PopPopOAuth2User;
import com.example.poppop.global.error.exception.CustomException;
import com.example.poppop.global.s3.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

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
    private final S3Service s3Service;

    @Override
    @Transactional
    public void create(Long popupId, ReviewCreateRequest request, PopPopOAuth2User oauth2User) {

        Long memberId = oauth2User.getMemberId();

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ReviewErrorCode.MEMBER_NOT_FOUND));

        Popup popup = popupRepository.findById(popupId)
                .orElseThrow(() -> new CustomException(ReviewErrorCode.POPUP_NOT_FOUND));

        Review review = Review.builder()
                .content(request.content())
                .member(member)
                .popup(popup)
                .build();

        request.images().forEach(file -> {
            String url = s3Service.uploadFile(file, "review-images");
            review.addImage(ReviewImage.of(url, review));
        });
        // 추후에 리뷰별로 이미지를 관리할 수 있도록 리팩터링

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
                    long likeCount = reviewLikeRepository.countByReviewAndLikedTrue(review);
                    long commentCount = commentRepository.countByReviewAndParentIsNullAndIsDeletedFalse(review);
                    return ReviewResponse.from(review, likeCount, commentCount);
                }).toList();
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

//    @Override
//    @Transactional
//    public void update(Long reviewId, ReviewUpdateRequest request, PopPopOAuth2User oauth2User) {
//
//        Review review = reviewRepository.findById(reviewId)
//                .orElseThrow(() -> new CustomException(ReviewErrorCode.REVIEW_NOT_FOUND));
//
//        if (!review.getMember().getId().equals(oauth2User.getMemberId())) {
//            throw new CustomException(ReviewErrorCode.INVALID_PERMISSION);
//        }
//
//        review.updateContent(request.content());
//    }

    @Override
    @Transactional
    public void update(Long reviewId, ReviewUpdateRequest request, PopPopOAuth2User oauth2User) {

        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new CustomException(ReviewErrorCode.REVIEW_NOT_FOUND));

        if (!review.getMember().getId().equals(oauth2User.getMemberId())) {
            throw new CustomException(ReviewErrorCode.INVALID_PERMISSION);
        }

        // 내용 업데이트 (null 이 아닐 때만)
        if (request.content() != null) {
            review.updateContent(request.content());
        }

        // 사진 업데이트
        List<MultipartFile> newImages = request.images();
        if (newImages != null && !newImages.isEmpty()) {
            // 기존 이미지 S3 + DB 삭제
            review.getImages().forEach(img -> {
                // S3 에서 오브젝트 삭제 (optional)
                s3Service.deleteFile(img.getUrl());
            });
            review.getImages().clear(); // orphanRemoval=true 이므로 DB에서도 삭제

            // 새 이미지 업로드 + 엔티티 연관
            String dir = "review-images/" + reviewId;
            for (MultipartFile file : newImages) {
                String url = s3Service.uploadFile(file, dir);
                review.addImage(ReviewImage.of(url, review));
            }
        }
    }

    @Override
    @Transactional
    public void delete(Long reviewId, PopPopOAuth2User oauth2User) {

        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new CustomException(ReviewErrorCode.REVIEW_NOT_FOUND));

        if (!review.getMember().getId().equals(oauth2User.getMemberId())) {
            throw new CustomException(ReviewErrorCode.INVALID_PERMISSION);
        }

        review.softDelete();
    }
}
