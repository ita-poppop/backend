package com.example.poppop.domain.story.repository;

import com.example.poppop.domain.popup.entity.Popup;
import com.example.poppop.domain.story.entity.Story;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoryRepository extends JpaRepository<Story, Long> {

    Page<Story> findAllByOrderByCreatedAtDesc(Pageable pageable);
    Page<Story> findByPopupOrderByCreatedAtDesc(Popup popup, Pageable pageable);
}
