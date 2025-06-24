package com.example.poppop.domain.comment.repository;

import com.example.poppop.domain.comment.entity.Comment;
import com.example.poppop.domain.comment.entity.CommentLike;
import com.example.poppop.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommentLikeRepository extends JpaRepository<CommentLike, Long> {

    Optional<CommentLike> findByCommentAndMember(Comment comment, Member member);

    long countByCommentAndLikedTrue(Comment comment);
}

