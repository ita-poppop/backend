package com.example.poppop.domain.comment.entity;

import com.example.poppop.domain.member.entity.Member;
import com.example.poppop.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(
        name = "comment_like",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_comment_member",
                columnNames = {"comment_id", "member_id"})
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommentLike extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Boolean liked = Boolean.TRUE;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "comment_id")
    private Comment comment;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "member_id")
    private Member member;

    private CommentLike(Member m, Comment c) {
        this.member  = m;
        this.comment = c;
    }
    public static CommentLike of(Member m, Comment c) { return new CommentLike(m, c); }

    public void toggle() { this.liked = !this.liked; }
}
