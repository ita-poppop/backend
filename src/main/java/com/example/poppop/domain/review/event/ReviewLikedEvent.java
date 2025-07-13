package com.example.poppop.domain.review.event;

import com.example.poppop.domain.review.entity.ReviewLike;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class ReviewLikedEvent extends ApplicationEvent {

    private final ReviewLike reviewLike;

    public ReviewLikedEvent(Object source, ReviewLike reviewLike) {
        super(source);
        this.reviewLike = reviewLike;
    }
}
