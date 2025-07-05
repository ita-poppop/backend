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
import com.example.poppop.domain.story.error.PopupErrorCode;
import com.example.poppop.domain.story.repository.StoryReadRepository;
import com.example.poppop.domain.story.repository.StoryRepository;
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
public class StoryServiceImpl implements StoryService {

    private final StoryRepository storyRepository;
    private final StoryReadRepository readRepository;
    private final MemberRepository memberRepository;
    private final PopupRepository popupRepository;

    @Override
    @Transactional
    public void create(Long popupId, StoryCreateRequest request, CustomOAuth2User oauth2User) {

        Member member = memberRepository.findById(oauth2User.getId())
                .orElseThrow(() -> new CustomException(PopupErrorCode.MEMBER_NOT_FOUND));
        Popup popup = popupRepository.findById(popupId)
                .orElseThrow(() -> new CustomException(PopupErrorCode.POPUP_NOT_FOUND));

        Story story = Story.builder()
                .photoUrl(request.photoUrl())
                .estimatedWaitTime(request.estimatedWaitTime())
                .estimatedWaitCount(request.estimatedWaitCount())
                .member(member)
                .popup(popup)
                .build();

        storyRepository.save(story);
    }

    @Override
    public List<StorySummaryResponse> findAllStory(int page, int size, CustomOAuth2User oauth2User) {

        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "createdAt"));

        return storyRepository.findAllByOrderByCreatedAtDesc(pageable).stream()
                .map(story -> {
                    boolean isRead = readRepository.existsByStoryAndMember(story, memberRepository.getReferenceById(oauth2User.getId()));
                    return new StorySummaryResponse(
                            story.getId(),
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
    public List<PopupStoryResponse> findByPopup(Long popupId, int page, int size, CustomOAuth2User oauth2User) {

        Popup popup = popupRepository.findById(popupId)
                .orElseThrow(() -> new CustomException(PopupErrorCode.POPUP_NOT_FOUND));
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "createdAt"));

        return storyRepository.findByPopupOrderByCreatedAtDesc(popup, pageable).stream()
                .map(story -> {
                    boolean isRead = readRepository.existsByStoryAndMember(story, memberRepository.getReferenceById(oauth2User.getId()));
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
    public StoryDetailResponse findOneStory(Long popupId, Long storyId, CustomOAuth2User oauth2User) {

        popupRepository.findById(popupId)
                .orElseThrow(() -> new CustomException(PopupErrorCode.POPUP_NOT_FOUND));
        Story story = storyRepository.findById(storyId)
                .orElseThrow(() -> new CustomException(PopupErrorCode.STORY_NOT_FOUND));

        boolean alreadyRead = readRepository.existsByStoryAndMember(
                story, memberRepository.getReferenceById(oauth2User.getId())
        );
        if (!alreadyRead) {
            readRepository.save(StoryRead.of(story, memberRepository.getReferenceById(oauth2User.getId())));
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
}
