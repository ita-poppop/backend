package com.example.poppop.domain.comment.repository;

import com.example.poppop.domain.comment.entity.Comment;
import com.example.poppop.domain.review.entity.Review;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    // 리뷰별 1차 댓글만 조회 → 자식은 fetch join
//    @Query("""
//        SELECT c FROM Comment c
//        LEFT JOIN FETCH c.children
//        WHERE c.review = :review
//          AND c.parent IS NULL
//          AND c.isDeleted = false
//        ORDER BY c.createdAt ASC
//    """)
//    List<Comment> findRootComments(@Param("review") Review review);

    // 페이징 처리를 위해 fetch join 제외
    @Query("""
      SELECT c FROM Comment c
      WHERE c.review = :review
        AND c.parent IS NULL
        AND c.isDeleted = false
      ORDER BY c.createdAt ASC
    """)
    List<Comment> findRootByReview(@Param("review") Review review, Pageable pageable);

    long countByReviewAndParentIsNullAndIsDeletedFalse(Review review);

    // 추가: parent(=루트 댓글)의 1단계 대댓글만 페이징 조회
    List<Comment> findByParentAndIsDeletedFalseOrderByCreatedAtAsc(
            Comment parent,
            Pageable pageable
    );
}
