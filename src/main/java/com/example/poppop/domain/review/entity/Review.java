package com.example.poppop.domain.review.entity;

import com.example.poppop.domain.member.entity.Member;
import com.example.poppop.domain.popup.entity.Popup;
import com.example.poppop.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "review")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(exclude = {"member", "popup"})
public class Review extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "popup_id")
    private Popup popup;

//    @OneToMany(mappedBy = "review",
//            cascade = CascadeType.ALL,
//            orphanRemoval = true)
//    private final List<Comment> comments = new ArrayList<>();
//
//    @OneToMany(mappedBy = "review",
//            cascade = CascadeType.ALL,
//            orphanRemoval = true)
//    private final List<ReviewLike> likes = new ArrayList<>();

    @Builder
    private Review(String content,
                   @NonNull Member member,
                   @NonNull Popup popup) {

        this.content = (content == null || content.isBlank()) ? "" : content;
        this.member  = member;
        this.popup   = popup;
    }

    public void updateContent(String content) {
        this.content = content;
    }

//    public void addComment(Comment comment) {
//        comments.add(comment);
//        comment.setReview(this);
//    }
//
//    public void addLike(ReviewLike like) {
//        likes.add(like);
//        like.setReview(this);
//    }

    @OneToMany(mappedBy = "review", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<ReviewImage> images = new ArrayList<>();

    public void addImage(ReviewImage image) {
        images.add(image);
    }
}

