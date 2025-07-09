package com.example.poppop.domain.review.repository;

import com.example.poppop.domain.member.entity.Member;
import com.example.poppop.domain.review.entity.Review;
import com.example.poppop.domain.review.entity.ReviewLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ReviewLikeRepository extends JpaRepository<ReviewLike, Long> {

    Optional<ReviewLike> findByReviewAndMember(Review review, Member member);

    long countByReviewAndLikedTrue(Review review);

    @Query("""
        SELECT COUNT(rl)
          FROM ReviewLike rl
         WHERE rl.review.member = :member
           AND rl.liked = true
    """)
    long sumLikesByMember(@Param("member") Member member);
}

