package com.example.poppop.global.auth.handler;

import com.example.poppop.global.auth.service.JwtService;
import com.example.poppop.global.common.ApiResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@RequiredArgsConstructor
@Component
public class JwtLogoutSuccessHandler implements LogoutSuccessHandler {
    private final JwtService jwtService;
    private final ObjectMapper objectMapper;

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        // 1. 헤더에서 Refresh Token 추출
        String bearerRefreshToken = request.getHeader("RefreshToken");

        // 2. 로그아웃 처리 (Refresh Token 무효화)
        jwtService.logout(bearerRefreshToken);

        // 3. 응답 세팅
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("application/json; charset=utf-8");
        response.setCharacterEncoding("UTF-8");

        // 4. 성공 메시지 반환
        ApiResponse<Object> success = ApiResponse.success("로그아웃이 완료되었습니다.");
        objectMapper.writeValue(response.getWriter(), success);
    }
}
