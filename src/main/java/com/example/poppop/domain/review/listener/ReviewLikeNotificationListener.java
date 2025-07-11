package com.example.poppop.domain.review.listener;

import com.example.poppop.domain.notification.service.NotificationService;
import com.example.poppop.domain.review.event.ReviewLikedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class ReviewLikeNotificationListener {

    private final NotificationService notificationService;

    @EventListener
    public void onReviewLiked(ReviewLikedEvent event) {
        var like   = event.getReviewLike();
        var review = like.getReview();
        var author = review.getMember();
        var liker  = like.getMember();

        // 본인 좋아요는 알림 보내지 않음
        if (author.getId().equals(liker.getId())) {
            return;
        }

        notificationService.sendToMember(
                author.getId(),
                "리뷰에 새 👍 좋아요!",
                liker.getUserName() + "님이 회원님의 리뷰를 좋아합니다.",
                Map.of(
                        "type",     "REVIEW_LIKE",
                        "reviewId", review.getId().toString()
                )
        );
    }
}
