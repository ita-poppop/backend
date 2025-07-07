//package com.example.poppop.domain.comment.listener;
//
//import com.example.poppop.domain.comment.entity.Comment;
//import com.example.poppop.domain.comment.event.CommentCreatedEvent;
//import com.example.poppop.domain.comment.event.ReplyCreatedEvent;
//import com.example.poppop.domain.notification.service.NotificationService;
//import com.example.poppop.domain.review.entity.Review;
//import lombok.RequiredArgsConstructor;
//import org.springframework.context.event.EventListener;
//import org.springframework.stereotype.Component;
//
//import java.util.Map;
//
//@Component
//@RequiredArgsConstructor
//public class CommentNotificationListener {
//
//    private final NotificationService notificationService;
//
//    @EventListener
//    public void onCommentCreated(CommentCreatedEvent event) {
//
//        Comment comment = event.getComment();
//        Review review = comment.getReview();
//
//        // 알림 대상·메시지 구성
//        Long targetId = review.getMember().getId();
//        if (!targetId.equals(comment.getMember().getId())) {
//            notificationService.sendToMember(
//                    targetId,
//                    "리뷰에 새 댓글이 달렸습니다",
//                    comment.getMember().getUserName() + "님이 댓글을 남겼어요.",
//                    Map.of(
//                            "type", "NEW_COMMENT",
//                            "reviewId", review.getId().toString(),
//                            "commentId", comment.getId().toString()
//                    )
//            );
//        }
//    }
//
//    @EventListener
//    public void onReplyCreated(ReplyCreatedEvent event) {
//        Comment reply  = event.getReply();
//        Comment parent = reply.getParent();
//
//        Long targetId = parent.getMember().getId();
//        if (!targetId.equals(reply.getMember().getId())) {
//            notificationService.sendToMember(
//                    targetId,
//                    "댓글에 새 대댓글이 달렸습니다",
//                    reply.getMember().getUserName() + "님이 답글을 달았어요.",
//                    Map.of(
//                            "type", "NEW_REPLY",
//                            "commentId", parent.getId().toString(),
//                            "replyId",   reply.getId().toString()
//                    )
//            );
//        }
//    }
//}
//
