package com.example.poppop.domain.story.repository;

import com.example.poppop.domain.member.entity.Member;
import com.example.poppop.domain.story.entity.Story;
import com.example.poppop.domain.story.entity.StoryRead;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoryReadRepository extends JpaRepository<StoryRead, Long> {

    boolean existsByStoryAndMember(Story story, Member member);
}
