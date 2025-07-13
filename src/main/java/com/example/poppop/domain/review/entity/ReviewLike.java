package com.example.poppop.domain.review.entity;

import com.example.poppop.domain.member.entity.Member;
import com.example.poppop.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(
        name = "review_like",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_review_member", columnNames = {"review_id", "member_id"})
) // 한 회원이 리뷰당 1줄만 갖도록 강제
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReviewLike extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /* 좋아요 on/off */
    @Column(nullable = false)
    private Boolean liked = Boolean.TRUE;      // 최초 생성 시 true 로

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "review_id")
    private Review review;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "member_id")
    private Member member;

    private ReviewLike(Member member, Review review) {
        this.member = member;
        this.review = review;
    }

    public static ReviewLike of(Member m, Review r) { return new ReviewLike(m, r); }

    public void toggle() { this.liked = !this.liked; }
}
