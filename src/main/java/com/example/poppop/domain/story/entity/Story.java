package com.example.poppop.domain.story.entity;

import com.example.poppop.domain.member.entity.Member;
import com.example.poppop.domain.popup.entity.Popup;
import com.example.poppop.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "stories")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Story extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String photoUrl;

    @Column(nullable = false)
    private int estimatedWaitTime;

    @Column(nullable = false)
    private int estimatedWaitCount;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "popup_id")
    private Popup popup;                   // 대상 팝업

    @Builder
    private Story(String photoUrl,
                  int estimatedWaitTime,
                  int estimatedWaitCount,
                  Member member,
                  Popup popup) {

        this.photoUrl          = photoUrl;
        this.estimatedWaitTime = estimatedWaitTime;
        this.estimatedWaitCount= estimatedWaitCount;
        this.member            = member;
        this.popup             = popup;
    }
}
