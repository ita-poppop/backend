package com.example.poppop.global.auth.service;

import com.example.poppop.domain.member.entity.Member;
import com.example.poppop.domain.member.repository.MemberRepository;
import com.example.poppop.global.auth.dto.TokenDto;
import com.example.poppop.global.auth.dto.UserInfo;
import com.example.poppop.global.auth.entity.RefreshToken;
import com.example.poppop.global.auth.model.PopPopOAuth2User;
import com.example.poppop.global.auth.repository.RefreshTokenRepository;
import com.example.poppop.global.error.GlobalErrorCode;
import com.example.poppop.global.error.exception.CustomException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.security.auth.message.AuthException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import static com.example.poppop.global.auth.service.JwtTokenProvider.BEARER_PREFIX;

@RequiredArgsConstructor
@Service
@Slf4j
public class JwtService {
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final MemberRepository memberRepository;
    private final RefreshTokenService refreshTokenService;

    public PopPopOAuth2User getPrincipal(String accessToken) {
        return PopPopOAuth2User.from(jwtTokenProvider.extractMemberDTOFromAccessTocken(accessToken));
    }

    @Transactional
    public void logout(String bearerRefreshTocken) {
        String email = this.extractEmailFromRefreshTocken(bearerRefreshTocken);
        log.debug("로그아웃 === 이메일: {}",  email);
        refreshTokenRepository.deleteAllByEmail(email);
    }

    private String extractEmailFromRefreshTocken(String bearerRefreshTocken) {
        String refreshTocken = this.getTockenFromBearer(bearerRefreshTocken);
        return jwtTokenProvider.getEmail(refreshTocken);
    }

    public void validate(String tocken) {
        try {
            jwtTokenProvider.getPayload(tocken);
        } catch (SecurityException e) {
            throw new CustomException(GlobalErrorCode.INTERNAL_SERVER_ERROR, "security exception");
        } catch (MalformedJwtException e) {
            throw new CustomException(GlobalErrorCode.INTERNAL_SERVER_ERROR, "malformed token");
        } catch (ExpiredJwtException e) {
            throw new CustomException(GlobalErrorCode.EXPIRED_TOKEN, "expired token");
        } catch (UnsupportedJwtException e)  {
            throw new CustomException(GlobalErrorCode.INVALID_TOKEN, "unsupported token");
        }
    }

    @Transactional
    public TokenDto tockenRefresh(String bearerRefreshTocken) {
        String refreshTocken = this.getTockenFromBearer(bearerRefreshTocken);
        this.validate(refreshTocken);
        PopPopOAuth2User popPopOAuth2User = this.extractPopPopOAuth2User(refreshTocken);
        log.debug("토큰 갱신 === 유저: {}", popPopOAuth2User);
        return this.doTockenGenerationProcess(popPopOAuth2User);
    }

    private PopPopOAuth2User extractPopPopOAuth2User(String refreshTocken) {
        RefreshToken refreshToken = refreshTokenRepository.findByPayload(refreshTocken)
                .orElseThrow(() -> new CustomException(GlobalErrorCode.BAD_REQUEST, "payload not found err"));
        Member memebr = memberRepository.findByEmail(refreshToken.getEmail())
                .orElseThrow(() -> new CustomException(GlobalErrorCode.NOT_FOUND, "email not found err"));
        return PopPopOAuth2User.from(UserInfo.from(memebr));
    }

    @Transactional
    public TokenDto doTockenGenerationProcess(PopPopOAuth2User principal) {
        TokenDto tockenDto = jwtTokenProvider.createJwt(principal);
        refreshTokenService.upsetRefreshTocken(tockenDto);
        return tockenDto;
    }

    /**
     * Bearer Prefix를 포함한 값을 전달받으면 토큰만을 추출하여 반환
     *
     * @param bearerTocken
     * @return Token (String)
     */
    public String getTockenFromBearer(String bearerTocken) {
        if(StringUtils.hasText(bearerTocken) && bearerTocken.startsWith(BEARER_PREFIX)) {
            return bearerTocken.split(" ")[1];
        }
        return null;
    }

}
