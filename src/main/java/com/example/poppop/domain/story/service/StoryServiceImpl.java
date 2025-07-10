package com.example.poppop.domain.story.service;

import com.example.poppop.domain.member.entity.CustomOAuth2User;
import com.example.poppop.domain.member.entity.Member;
import com.example.poppop.domain.member.repository.MemberRepository;
import com.example.poppop.domain.popup.entity.Popup;
import com.example.poppop.domain.popup.repository.PopupRepository;
import com.example.poppop.domain.story.dto.request.StoryCreateRequest;
import com.example.poppop.domain.story.dto.response.PopupStoryResponse;
import com.example.poppop.domain.story.dto.response.StoryDetailResponse;
import com.example.poppop.domain.story.dto.response.StorySummaryResponse;
import com.example.poppop.domain.story.entity.Story;
import com.example.poppop.domain.story.entity.StoryRead;
import com.example.poppop.domain.story.error.StoryErrorCode;
import com.example.poppop.domain.story.repository.StoryReadRepository;
import com.example.poppop.domain.story.repository.StoryRepository;
import com.example.poppop.global.auth.model.PopPopOAuth2User;
import com.example.poppop.global.error.exception.CustomException;
import com.example.poppop.global.s3.service.S3Service;
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
public class StoryServiceImpl implements StoryService {

    private final S3Service s3Service;
    private final StoryRepository storyRepository;
    private final StoryReadRepository readRepository;
    private final MemberRepository memberRepository;
    private final PopupRepository popupRepository;

    @Override
    @Transactional
    public void create(Long popupId, StoryCreateRequest request, PopPopOAuth2User oauth2User) {

        Member member = memberRepository.findById(oauth2User.getMemberId())
                .orElseThrow(() -> new CustomException(StoryErrorCode.MEMBER_NOT_FOUND));
        Popup popup = popupRepository.findById(popupId)
                .orElseThrow(() -> new CustomException(StoryErrorCode.POPUP_NOT_FOUND));

        // S3 업로드하고 URL 받기
        String photoUrl = s3Service.uploadFile(request.photo(), "stories");

        Story story = Story.builder()
//                .photoUrl(request.photoUrl())
                .photoUrl(photoUrl)
                .estimatedWaitTime(request.estimatedWaitTime())
                .estimatedWaitCount(request.estimatedWaitCount())
                .member(member)
                .popup(popup)
                .build();

        storyRepository.save(story);
    }

    @Override
    public List<StorySummaryResponse> findAllStory(int page, int size, PopPopOAuth2User oauth2User) {

        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "createdAt"));

        return storyRepository.findAllByOrderByCreatedAtDesc(pageable).stream()
                .map(story -> {
                    boolean isRead = readRepository.existsByStoryAndMember(story, memberRepository.getReferenceById(oauth2User.getMemberId()));
                    return new StorySummaryResponse(
                            story.getId(),
                            story.getPopup().getId(),
                            story.getPhotoUrl(),
                            story.getEstimatedWaitTime(),
                            story.getEstimatedWaitCount(),
                            story.getPopup().getTitle(),
                            story.getPopup().getLocation(),
                            story.getMember().getUserName(),
                            story.getMember().getProfileUrl(),
                            isRead,
                            story.getCreatedAt()
                    );
                })
                .toList();
    }

    @Override
    public List<PopupStoryResponse> findByPopup(Long popupId, int page, int size, PopPopOAuth2User oauth2User) {

        Popup popup = popupRepository.findById(popupId)
                .orElseThrow(() -> new CustomException(StoryErrorCode.POPUP_NOT_FOUND));
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "createdAt"));

        return storyRepository.findByPopupOrderByCreatedAtDesc(popup, pageable).stream()
                .map(story -> {
                    boolean isRead = readRepository.existsByStoryAndMember(story, memberRepository.getReferenceById(oauth2User.getMemberId()));
                    return new PopupStoryResponse(
                            story.getId(),
                            story.getPhotoUrl(),
                            story.getMember().getUserName(),
                            story.getMember().getProfileUrl(),
                            isRead,
                            story.getCreatedAt()
                    );
                })
                .toList();
    }

    @Override
    @Transactional
    public StoryDetailResponse findOneStory(Long popupId, Long storyId, PopPopOAuth2User oauth2User) {

        popupRepository.findById(popupId)
                .orElseThrow(() -> new CustomException(StoryErrorCode.POPUP_NOT_FOUND));
        Story story = storyRepository.findById(storyId)
                .orElseThrow(() -> new CustomException(StoryErrorCode.STORY_NOT_FOUND));

        boolean alreadyRead = readRepository.existsByStoryAndMember(
                story, memberRepository.getReferenceById(oauth2User.getMemberId())
        );
        if (!alreadyRead) {
            readRepository.save(StoryRead.of(story, memberRepository.getReferenceById(oauth2User.getMemberId())));
        }

        return new StoryDetailResponse(
                story.getId(),
                story.getPhotoUrl(),
                story.getMember().getUserName(),
                story.getMember().getProfileUrl(),
                story.getPopup().getTitle(),
                story.getCreatedAt()
        );
    }

    @Override
    @Transactional
    public void delete(Long popupId, Long storyId, PopPopOAuth2User oauth2User) {

        popupRepository.findById(popupId)
                .orElseThrow(() -> new CustomException(StoryErrorCode.POPUP_NOT_FOUND));

        Story story = storyRepository.findById(storyId)
                .orElseThrow(() -> new CustomException(StoryErrorCode.STORY_NOT_FOUND));

        if (!story.getMember().getId().equals(oauth2User.getMemberId())) {
            throw new CustomException(StoryErrorCode.INVALID_PERMISSION);
        }

//        story.softDelete();
        // 1) 기존 사진 S3에서 삭제
        s3Service.deleteFile(story.getPhotoUrl());
        // 2) 스토리 하드 삭제
        storyRepository.delete(story);
    }
}
