package com.example.poppop.domain.bookmark.service;

import com.example.poppop.domain.bookmark.dto.response.PopupBookmarkResponse;
import com.example.poppop.domain.bookmark.entity.Bookmark;
import com.example.poppop.domain.bookmark.error.BookmarkErrorCode;
import com.example.poppop.domain.bookmark.repository.BookmarkRepository;
import com.example.poppop.domain.member.entity.Member;
import com.example.poppop.domain.member.repository.MemberRepository;
import com.example.poppop.domain.popup.entity.Popup;
import com.example.poppop.domain.popup.repository.PopupRepository;
import com.example.poppop.global.auth.model.PopPopOAuth2User;
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
public class BookmarkServiceImpl implements BookmarkService {

    private final BookmarkRepository bookmarkRepository;
    private final MemberRepository memberRepository;
    private final PopupRepository popupRepository;

    @Override
    public List<PopupBookmarkResponse> findAllBookmarks(PopPopOAuth2User oAuth2User, int page, int size) {

        Member member = memberRepository.findById(oAuth2User.getMemberId())
                .orElseThrow(() -> new CustomException(BookmarkErrorCode.MEMBER_NOT_FOUND));

        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "createdAt"));

        return bookmarkRepository.findAllByMember(member, pageable).stream()
                .map(bmk -> PopupBookmarkResponse.from(bmk.getPopup()))
                .toList();
    }

    @Override
    @Transactional
    public void toggleBookmark(Long popupId, PopPopOAuth2User oAuth2User) {

        Member member = memberRepository.findById(oAuth2User.getMemberId())
                .orElseThrow(() -> new CustomException(BookmarkErrorCode.MEMBER_NOT_FOUND));
        Popup popup = popupRepository.findById(popupId)
                .orElseThrow(() -> new CustomException(BookmarkErrorCode.POPUP_NOT_FOUND));

        bookmarkRepository.findByMemberAndPopup(member, popup)
                .ifPresentOrElse(
                        bookmarkRepository::delete,
                        () -> bookmarkRepository.save(new Bookmark(member, popup))
                );
    }

    @Override
    @Transactional
    public void deleteBookmark(Long popupId, PopPopOAuth2User oAuth2User) {

        Member member = memberRepository.findById(oAuth2User.getMemberId())
                .orElseThrow(() -> new CustomException(BookmarkErrorCode.MEMBER_NOT_FOUND));
        Popup popup = popupRepository.findById(popupId)
                .orElseThrow(() -> new CustomException(BookmarkErrorCode.POPUP_NOT_FOUND));

        boolean existed = bookmarkRepository.findByMemberAndPopup(member, popup)
                .map(bmk -> { bookmarkRepository.delete(bmk); return true; })
                .orElse(false);

        if (!existed) {
            throw new CustomException(BookmarkErrorCode.FAVORITE_NOT_FOUND);
        }
    }
}
