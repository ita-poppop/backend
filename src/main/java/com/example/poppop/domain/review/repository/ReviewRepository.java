package com.example.poppop.domain.review.repository;

import com.example.poppop.domain.popup.entity.Popup;
import com.example.poppop.domain.review.entity.Review;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findByPopupAndIsDeletedFalseOrderByCreatedAtDesc(Popup popup, Pageable pageable);
}
