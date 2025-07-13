package com.example.poppop.domain.bookmark.entity;

import com.example.poppop.domain.member.entity.Member;
import com.example.poppop.domain.popup.entity.Popup;
import com.example.poppop.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(
        name = "bookmarks",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_bookmark_member_popup",
                columnNames = {"member_id","popup_id"}
        )
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Bookmark extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "popup_id")
    private Popup popup;

    public Bookmark(Member member, Popup popup) {
        this.member = member;
        this.popup  = popup;
    }
}
