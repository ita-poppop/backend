package com.example.poppop.domain.comment.entity;

import com.example.poppop.domain.member.entity.Member;
import com.example.poppop.domain.review.entity.Review;
import com.example.poppop.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "comments")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(exclude = {"member", "review", "parent"})
public class Comment extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "review_id")
    private Review review;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Comment parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("createdAt ASC")
    private final List<Comment> children = new ArrayList<>();

    @Builder
    private Comment(String content,
                    Member member,
                    Review review,
                    Comment parent) {

        this.content = content;
        this.member  = member;
        this.review  = review;
        this.parent  = parent;
    }

    public void updateContent(String content) { this.content = content; }

    public void addChild(Comment child) {
        children.add(child);
    }
}
