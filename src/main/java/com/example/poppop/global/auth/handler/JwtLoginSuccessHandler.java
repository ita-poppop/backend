package com.example.poppop.global.auth.handler;

import com.example.poppop.domain.member.service.MemberService;
import com.example.poppop.global.auth.dto.TokenDto;
import com.example.poppop.global.auth.model.PopPopOAuth2User;
import com.example.poppop.global.auth.service.JwtService;
import com.example.poppop.global.auth.service.JwtTokenProvider;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.nio.charset.StandardCharsets;


/*@RequiredArgsConstructor
@Component
public class JwtLoginSuccessHandler implements AuthenticationSuccessHandler {

    @Value("${client.url}")
    private String clientUrl; //OAuth2 로그인 성공 후 사용자를 리다이렉트할 프론트엔드 애플리케이션의 URL입니다.

    private final JwtService jwtService;
    private final JwtTokenProvider jwtTokenProvider;
    private final MemberService memberService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        PopPopOAuth2User principal = (PopPopOAuth2User) authentication.getPrincipal();
        memberService.registerIfNotExists(principal);
        TokenDto tokenDto = jwtService.doTockenGenerationProcess(principal);

        // 헤더에 토큰 추가
        response.setHeader("Authorization", "Bearer " + tokenDto.getAccessTocken());
        response.setHeader("RefreshToken", tokenDto.getRefreshTocken());

*//*        //json 응답 둘중에 하나 선택해서 필요한거 쓰게 하고 나중에 수정
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(
                String.format("{\"accessToken\":\"%s\",\"refreshToken\":\"%s\"}",
                        tokenDto.getAccessTocken(),
                        tokenDto.getRefreshTocken())
        );*//*

        response.sendRedirect(createUri());
    }

    private String createUri() {
        return UriComponentsBuilder
                .fromUriString(clientUrl)
                .build()
                .encode(StandardCharsets.UTF_8)
                .toUriString();
    }
}*/

