package com.example.poppop.domain.mypage.service;

import com.example.poppop.domain.member.entity.Member;
import com.example.poppop.domain.member.repository.MemberRepository;
import com.example.poppop.domain.mypage.dto.request.ProfileUpdateRequest;
import com.example.poppop.domain.mypage.dto.response.ProfileResponseDto;
import com.example.poppop.domain.mypage.error.MemberErrorCode;
import com.example.poppop.domain.popup.entity.Popup;
import com.example.poppop.domain.review.entity.Review;
import com.example.poppop.domain.review.repository.ReviewLikeRepository;
import com.example.poppop.domain.review.repository.ReviewRepository;
import com.example.poppop.global.auth.model.PopPopOAuth2User;
import com.example.poppop.global.error.exception.CustomException;
import com.example.poppop.global.s3.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProfileServiceImpl implements ProfileService {

    private final MemberRepository memberRepository;
    private final ReviewRepository reviewRepository;
    private final ReviewLikeRepository reviewLikeRepository;
    private final S3Service s3Service;

    @Override
    public ProfileResponseDto getMyProfile(PopPopOAuth2User oauth2User) {

        Member member = memberRepository.findById(oauth2User.getMemberId())
                .orElseThrow(() -> new CustomException(MemberErrorCode.MEMBER_NOT_FOUND));

        // 내 리뷰 목록
        List<Review> myReviews = reviewRepository
                .findByMemberAndIsDeletedFalseOrderByCreatedAtDesc(member);

        // DTO 변환: 팝업 정보 + 내가 쓴 리뷰 총 개수
        List<ProfileResponseDto.ProfileReviewDto> reviews = myReviews.stream()
                .map(review -> {
                    Popup popup = review.getPopup();
                    // 팝업 대표 이미지: 리뷰 이미지 중 첫 번째 URL 또는 팝업 자체 필드
                    String popupImg = popup.getImage();
                    return new ProfileResponseDto.ProfileReviewDto(
                            review.getId(),
                            popupImg,
                            popup.getTitle(),
                            popup.getStartDate(),
                            popup.getEndDate()
                    );
                })
                .collect(Collectors.toList());

        long reviewCount   = myReviews.size();
        long totalLikes    = myReviews.stream()
                .mapToLong(r -> reviewLikeRepository.countByReviewAndLikedTrue(r))
                .sum();

        return ProfileResponseDto.of(
                member.getProfileUrl(),
                member.getUserName(),
                reviewCount,
                totalLikes,
                reviews
        );
    }

    @Override
    @Transactional
    public void updateMyProfile(
            PopPopOAuth2User oauth2User,
            ProfileUpdateRequest request
    ) {
        Member member = memberRepository.findById(oauth2User.getMemberId())
                .orElseThrow(() -> new CustomException(MemberErrorCode.MEMBER_NOT_FOUND));

        // 1) 프로필 사진만 바꿀 경우
        if (request.profileImage() != null
                && !request.profileImage().isEmpty()) {

            String newProfileUrl = s3Service.uploadFile(
                    request.profileImage(),
                    "profile-image"
            );
            member.updateProfile(newProfileUrl, member.getUserName());
        }

        // 2) 닉네임만 바꿀 경우 (빈 문자열이 아니라는 전제하에)
        if (request.userName() != null
                && !request.userName().isBlank()
                && !request.userName().equals(member.getUserName())) {

            member.updateProfile(member.getProfileUrl(), request.userName());
        }
    }
}
