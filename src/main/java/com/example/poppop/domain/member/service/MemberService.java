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
import com.example.poppop.global.auth.service.JwtTokenProvider;
import com.example.poppop.global.auth.service.RefreshTokenService;
import com.example.poppop.global.error.GlobalErrorCode;
import com.example.poppop.global.error.exception.CustomException;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberService {

    private final MemberRepository memberRepository;
    private final JwtService jwtService;
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenService refreshTokenService;

    @Transactional
    public TokenDto signupAndAuthenticate(UserInfo userInfo, HttpServletResponse response) {
        Member member = registerIfNotExists(userInfo);

        PopPopOAuth2User principal = new PopPopOAuth2User(member.getId());

        String accessTocken = jwtTokenProvider.generateAccessTocken(member, new Date());
        String refreshTocken = jwtTokenProvider.generateRefreshTocken(member, new Date());
        refreshTokenService.upsetRefreshTocken(member,refreshTocken);

        response.setHeader("Authorization", "Bearer " + accessTocken);
        response.setHeader("RefreshToken", refreshTocken);

        return TokenDto.of(member, accessTocken, refreshTocken);
    }

    private Member registerIfNotExists(UserInfo userInfo) {
        return memberRepository.findByEmail(userInfo.getEmail())
                .orElseGet(() -> memberRepository.save(Member.builder()
                        .providerId(userInfo.getProviderId())
                        .registerId(userInfo.getRegisterId())
                        .email(userInfo.getEmail())
                        .nickName(userInfo.getNickName())
                        .profileImage(userInfo.getProfileImage())
                        .userName(userInfo.getNickName()) // 또는 다른 적절한 값
                        .build()));
    }

    @Transactional(readOnly = true)
    public MemberResponse getMemberResponse(PopPopOAuth2User user) {
        Long memberId = user.getMemberId();
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(GlobalErrorCode.NOT_FOUND, "토큰으로 멤버를 찾을 수 없습니다."));
        return MemberResponse.from(member);
    }

    @Transactional(readOnly = true)
    public Member getMemberByAccessToken(String token) {
        if (token == null) {
            throw new CustomException(GlobalErrorCode.UNAUTHORIZED, "토큰이 없습니다");
        }
        Long memberId = jwtService.getMemberIdFromAccessToken(token);
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(GlobalErrorCode.NOT_FOUND, "토큰으로 멤버를 찾을 수 없습니다."));
        return member;
    }
}
