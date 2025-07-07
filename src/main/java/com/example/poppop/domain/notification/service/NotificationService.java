package com.example.poppop.domain.notification.service;

import com.example.poppop.domain.member.repository.MemberRepository;
import com.example.poppop.domain.notification.entity.DeviceToken;
import com.example.poppop.domain.notification.repository.DeviceTokenRepository;
import com.google.firebase.messaging.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final FirebaseMessaging fcm;
    private final DeviceTokenRepository tokenRepository;
    private final MemberRepository memberRepository;

    /**
     * 특정 회원에게 단독 푸시
     */
    public void sendToMember(Long memberId, String title, String body, Map<String, String> data) {
        List<DeviceToken> tokens = tokenRepository.findAllByMember(
                memberRepository.getReferenceById(memberId));

        if (tokens.isEmpty()) return;

        MulticastMessage message = MulticastMessage.builder()
                .setNotification(
                        Notification.builder()
                                .setTitle(title)
                                .setBody(body)
                                .build()
                )
                .putAllData(data)
                .addAllTokens(tokens.stream()
                        .map(DeviceToken::getToken)
                        .toList())
                .build();

        try {
            BatchResponse response = fcm.sendMulticast(message);
            // 실패한 토큰 처리(Optional)
        } catch (FirebaseMessagingException e) {
            // 로깅 등 에러 처리
        }
    }

    /**
     * 주제(topic) 푸시 (ex. "news", "all")
     */
    public void sendToTopic(String topic, String title, String body) {
        Message message = Message.builder()
                .setTopic(topic)
                .setNotification(
                        Notification.builder()
                                .setTitle(title)
                                .setBody(body)
                                .build()
                )
                .build();

        try {
            fcm.send(message);
        } catch (FirebaseMessagingException e) {
            // 에러 처리
        }
    }
}

