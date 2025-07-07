//package com.example.poppop.domain.notification.service;
//
//import com.example.poppop.domain.member.entity.CustomOAuth2User;
//import com.example.poppop.domain.member.entity.Member;
//import com.example.poppop.domain.member.repository.MemberRepository;
//import com.example.poppop.domain.notification.entity.DeviceToken;
//import com.example.poppop.domain.notification.repository.DeviceTokenRepository;
//import com.example.poppop.global.auth.model.PopPopOAuth2User;
//import com.example.poppop.global.error.GlobalErrorCode;
//import com.example.poppop.global.error.exception.CustomException;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//@Service
//@RequiredArgsConstructor
//@Transactional
//public class FcmService {
//
//    private final DeviceTokenRepository tokenRepository;
//    private final MemberRepository memberRepository;
//
//    public void registerToken(PopPopOAuth2User oauth2User, String token) {
//        Member member = memberRepository.findById(oauth2User.getMemberId())
//                .orElseThrow(() -> new CustomException(GlobalErrorCode.NOT_FOUND));
//
//        if (!tokenRepository.existsByMemberAndToken(member, token)) {
//            tokenRepository.save(new DeviceToken(member, token));
//        }
//    }
//
//    public void removeToken(PopPopOAuth2User oauth2User, String token) {
//        Member member = memberRepository.findById(oauth2User.getMemberId())
//                .orElseThrow(() -> new CustomException(GlobalErrorCode.NOT_FOUND));
//
//        tokenRepository.deleteByMemberAndToken(member, token);
//    }
//}
//
