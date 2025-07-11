package com.example.poppop.domain.notification.service;

import com.example.poppop.domain.member.repository.MemberRepository;
import com.example.poppop.domain.notification.entity.DeviceToken;
import com.example.poppop.domain.notification.repository.DeviceTokenRepository;
import com.google.firebase.messaging.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {

    private final FirebaseMessaging fcm;
    private final DeviceTokenRepository tokenRepository;
    private final MemberRepository memberRepository;

    /**
     * 특정 회원에게 단독 푸시
     * - 여러 기기 토큰에 한번에 전송
     * - 전송 결과(BatchResponse)를 그대로 반환
     * - 예외는 구체적으로 분기하여 처리
     */
    public BatchResponse sendToMember(Long memberId, String title, String body, Map<String, String> data) {
        List<DeviceToken> tokens = tokenRepository.findAllByMember(
                memberRepository.getReferenceById(memberId));

        if (tokens.isEmpty()) return null;

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

            // 각 토큰별 전송 결과에 대해 구체적으로 예외 처리
            List<SendResponse> responses = response.getResponses();
            for (int i = 0; i < responses.size(); i++) {
                SendResponse sendResponse = responses.get(i);
                if (!sendResponse.isSuccessful()) {
                    String failedToken = tokens.get(i).getToken();
                    FirebaseMessagingException ex = (FirebaseMessagingException) sendResponse.getException();
                    MessagingErrorCode errorCode = ex.getMessagingErrorCode();

                    if (errorCode == MessagingErrorCode.INVALID_ARGUMENT) {
                        log.warn("유효하지 않은 FCM 토큰: {}", failedToken);
                        tokenRepository.delete(tokens.get(i));
                    } else if (errorCode == MessagingErrorCode.UNREGISTERED) {
                        log.warn("만료되었거나 등록 해제된 FCM 토큰: {}", failedToken);
                        tokenRepository.delete(tokens.get(i));
                    } else if (errorCode == MessagingErrorCode.SENDER_ID_MISMATCH) {
                        log.error("FCM Sender ID 불일치: {}", failedToken);
                    } else {
                        log.error("FCM 전송 실패: 토큰={}, 오류={}", failedToken, errorCode);
                    }
                }
            }
            return response;
        } catch (FirebaseMessagingException e) {
            // 전체 전송 자체가 실패한 경우
            log.error("FCM 멀티캐스트 전체 전송 실패", e);
            throw new RuntimeException("FCM 전송 중 오류 발생", e);
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
            log.error("FCM 토픽 전송 실패", e);
        }
    }
}
