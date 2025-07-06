package com.example.poppop.global.auth.filter;

import com.example.poppop.global.auth.dto.PopPopUserDetails;
import com.example.poppop.global.auth.dto.UserInfo;
import com.example.poppop.global.auth.model.PopPopOAuth2User;
import com.example.poppop.global.auth.service.JwtService;
import com.example.poppop.global.auth.service.JwtTokenProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
@Component
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");

        if (StringUtils.hasText(authHeader) && authHeader.startsWith(JwtTokenProvider.BEARER_PREFIX)) {
            String token = authHeader.substring(JwtTokenProvider.BEARER_PREFIX.length()).trim();

            try {
                // 토큰 유효성 검증
                jwtService.validate(token);

                // 토큰에서 사용자 정보 추출
                UserInfo userInfo = jwtTokenProvider.extractMemberDTOFromAccessTocken(token);

                // UserDetails 구현체(PopPopOAuth2User) 생성
                PopPopOAuth2User userDetails = PopPopOAuth2User.from(userInfo);

                // 인증 객체 생성 및 SecurityContext 등록
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                SecurityContextHolder.getContext().setAuthentication(authentication);
            } catch (Exception e) {
                log.warn("JWT 인증 실패: {}", e.getMessage());
            }
        }

        filterChain.doFilter(request, response);
    }
}
