package com.example.poppop.domain.story.entity;

import com.example.poppop.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.*;
import com.example.poppop.global.entity.BaseEntity;

@Entity
@Table(
        name = "story_read",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_story_member",
                columnNames = {"story_id","member_id"}
        )
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StoryRead extends BaseEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "story_id")
    private Story story;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "member_id")
    private Member member;

    private StoryRead(Story story, Member member) {
        this.story  = story;
        this.member = member;
    }

    public static StoryRead of(Story story, Member member) {
        return new StoryRead(story, member);
    }
}
