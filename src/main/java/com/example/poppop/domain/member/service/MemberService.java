package com.example.poppop.domain.member.service;

import com.example.poppop.domain.member.dto.MemberRequest;
import com.example.poppop.domain.member.dto.MemberResponse;
import com.example.poppop.domain.member.entity.Member;
import com.example.poppop.domain.member.repository.MemberRepository;
import com.example.poppop.global.auth.dto.PopPopUserDetails;
import com.example.poppop.global.auth.dto.TokenDto;
import com.example.poppop.global.auth.dto.UserInfo;
import com.example.poppop.global.auth.model.PopPopOAuth2User;
import com.example.poppop.global.auth.service.JwtService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberService {

    private final MemberRepository memberRepository;
    private final JwtService jwtService;

    @Transactional
    public TokenDto signupAndAuthenticate(UserInfo userInfo, HttpServletResponse response) {
        // 1. 회원 생성 또는 기존 회원 반환
        Member member = registerIfNotExists(PopPopOAuth2User.from(userInfo));

        // 2. PopPopOAuth2User 생성 (UserDetails 역할)
        PopPopOAuth2User principal = PopPopOAuth2User.from(userInfo);

        // 3. JWT 토큰 발급
        TokenDto tokenDto = jwtService.doTockenGenerationProcess(principal);

        // 4. SecurityContext에 인증 정보 저장 (UserDetails 구현체로 PopPopOAuth2User 사용)
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(principal, null, principal.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // 5. 토큰을 헤더에 추가 (선택)
        response.setHeader("Authorization", "Bearer " + tokenDto.getAccessTocken());
        response.setHeader("RefreshToken", tokenDto.getRefreshTocken());

        // 6. 토큰 반환
        return tokenDto;
    }

    // 회원 정보 조회 (MemberResponse로 변환)
    @Transactional(readOnly = true)
    public MemberResponse getMemberInfo(String email) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("해당 이메일의 회원이 존재하지 않습니다."));
        return MemberResponse.from(member);
    }

    // 회원 존재 여부 확인
    @Transactional(readOnly = true)
    public boolean existsByEmail(String email) {
        return memberRepository.findByEmail(email).isPresent();
    }

    /**
     * 소셜 로그인 정보로 회원을 생성하거나, 이미 있으면 기존 회원 반환
     */
    @Transactional
    public Member registerIfNotExists(PopPopOAuth2User oAuth2User) {
        return memberRepository.findByEmail(oAuth2User.getEmail())
                .orElseGet(() -> memberRepository.save(Member.builder()
                        .providerId(oAuth2User.getProviderId())
                        .registerId(oAuth2User.getRegisterId())
                        .email(oAuth2User.getEmail())
                        .nickName(oAuth2User.getNickName())
                        .profileImage(oAuth2User.getProfileImage())
                        .userName(oAuth2User.getNickName()) // 또는 다른 적절한 값
                        .build()));
    }
}
