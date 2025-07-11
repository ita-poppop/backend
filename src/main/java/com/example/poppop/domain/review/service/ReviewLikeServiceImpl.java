package com.example.poppop.domain.review.service;

import com.example.poppop.domain.member.entity.CustomOAuth2User;
import com.example.poppop.domain.member.entity.Member;
import com.example.poppop.domain.member.repository.MemberRepository;
import com.example.poppop.domain.review.dto.response.ReviewLikeResponse;
import com.example.poppop.domain.review.entity.Review;
import com.example.poppop.domain.review.entity.ReviewLike;
import com.example.poppop.domain.review.error.ReviewLikeErrorCode;
import com.example.poppop.domain.review.event.ReviewLikedEvent;
import com.example.poppop.domain.review.repository.ReviewLikeRepository;
import com.example.poppop.domain.review.repository.ReviewRepository;
import com.example.poppop.global.auth.model.PopPopOAuth2User;
import com.example.poppop.global.error.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ReviewLikeServiceImpl implements ReviewLikeService {

    private final ReviewLikeRepository reviewLikeRepository;
    private final ReviewRepository reviewRepository;
    private final MemberRepository memberRepository;
    private final ApplicationEventPublisher publisher;

    @Override
    public ReviewLikeResponse toggle(Long reviewId, PopPopOAuth2User oauth2User) {

        Member member = memberRepository.findById(oauth2User.getMemberId())
                .orElseThrow(() -> new CustomException(ReviewLikeErrorCode.MEMBER_NOT_FOUND));

        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new CustomException(ReviewLikeErrorCode.REVIEW_NOT_FOUND));

        ReviewLike like = reviewLikeRepository.findByReviewAndMember(review, member)
                .orElseGet(() -> reviewLikeRepository.save(ReviewLike.of(member, review)));

        like.toggle();
        long cnt = reviewLikeRepository.countByReviewAndLikedTrue(review);

        if (like.getLiked()
                && !review.getMember().getId().equals(member.getId())) {
            publisher.publishEvent(new ReviewLikedEvent(this, like));
        }

        return new ReviewLikeResponse(like.getLiked(), cnt);
    }
}

