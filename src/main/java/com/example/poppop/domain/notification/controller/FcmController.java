//package com.example.poppop.domain.notification.controller;
//
//import com.example.poppop.domain.member.entity.CustomOAuth2User;
//import com.example.poppop.domain.notification.dto.request.FcmTokenRequest;
//import com.example.poppop.domain.notification.service.FcmService;
//import com.example.poppop.global.auth.model.PopPopOAuth2User;
//import com.example.poppop.global.common.ApiResponse;
//import jakarta.validation.Valid;
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.core.annotation.AuthenticationPrincipal;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@RequestMapping("/api/v1/fcm")
//@RequiredArgsConstructor
//public class FcmController {
//
//    private final FcmService fcmService;
//
//    /** 토큰 등록 */
//    @PostMapping("/token")
//    public ApiResponse<Void> registerToken(
//            @RequestBody @Valid FcmTokenRequest request,
//            @AuthenticationPrincipal PopPopOAuth2User oauth2User) {
//
//        fcmService.registerToken(oauth2User, request.token());
//        return ApiResponse.successMessage("토큰 등록 완료");
//    }
//
//    /** 토큰 삭제 (앱 로그아웃 등) */
//    @DeleteMapping("/token")
//    public ApiResponse<Void> removeToken(
//            @RequestBody @Valid FcmTokenRequest request,
//            @AuthenticationPrincipal PopPopOAuth2User oauth2User) {
//
//        fcmService.removeToken(oauth2User, request.token());
//        return ApiResponse.successMessage("토큰 삭제 완료");
//    }
//}
