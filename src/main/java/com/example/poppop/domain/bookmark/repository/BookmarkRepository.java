package com.example.poppop.domain.bookmark.repository;

import com.example.poppop.domain.bookmark.entity.Bookmark;
import com.example.poppop.domain.member.entity.Member;
import com.example.poppop.domain.popup.entity.Popup;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {

    Optional<Bookmark> findByMemberAndPopup(Member member, Popup popup);
    List<Bookmark> findAllByMember(Member member);
    void deleteByMemberAndPopup(Member member, Popup popup);
}
