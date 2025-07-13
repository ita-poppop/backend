package com.example.poppop.global.auth.service;

import com.example.poppop.domain.member.entity.Member;
import com.example.poppop.global.auth.dto.TokenDto;
import com.example.poppop.global.auth.dto.UserInfo;
import com.example.poppop.global.auth.model.PopPopOAuth2User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
public class JwtTokenProvider {

    public static final String BEARER_PREFIX = "Bearer ";

    private final Long accessTockenExpiration;
    private final Long refreshTockenExpiration;
    private final SecretKey secretKey;
    private final JwtParser jwtParser;

    public JwtTokenProvider(
            @Value("${jwt.access-tocken-expiration}") Long accessTockenExpiration,
            @Value("${jwt.refresh-tocken-expiration}") Long refreshTockenExpiration,
            @Value("${jwt.secret}") String secretKey
    ) {
        this.accessTockenExpiration = accessTockenExpiration;
        this.refreshTockenExpiration = refreshTockenExpiration;
        this.secretKey = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), Jwts.SIG.HS256.key().build().getAlgorithm());
        this.jwtParser = Jwts.parser()
                .verifyWith(this.secretKey)
                .build();
    }

/*    public TokenDto createJwt(PopPopOAuth2User popPopOAuth2User, Long memebrId) {
        String accessTocken=generateAccessTocken(popPopOAuth2User, memebrId ,new Date());
        String refreshTocken = generateRefreshTocken(popPopOAuth2User.getProviderId(), new Date());

        return TokenDto.builder()
                .email(popPopOAuth2User.getEmail())
                .(accessTocken)
                .refreshTocken(refreshTocken)
                .build();
    }*/

    public String generateAccessTocken(Member member, Date now) {
        return Jwts.builder()
                .subject(member.getNickName())
                .claim("memberId", member.getId())
                .claim("providerId", member.getProviderId())
                .claim("nickName", member.getNickName())
                .claim("email", member.getEmail())
                .claim("profileImage",member.getProfileImage())
                .issuedAt(now) // 지금 시각이 발급 시각
                .expiration(new Date(now.getTime() + accessTockenExpiration))
                .signWith(secretKey)
                .compact();
    }

    public String generateRefreshTocken(Member member, Date now) {
        return Jwts.builder()
                .subject(String.valueOf(member.getId()))
                .issuedAt(now)
                .expiration(new Date(now.getTime() + refreshTockenExpiration))
                .signWith(secretKey)
                .compact();
    }

    public Long getAccessTockenExpiration() {
        return accessTockenExpiration;
    }

    public Long getRefreshTockenExpiration() {
        return refreshTockenExpiration;
    }

/*
    public UserInfo extractMemberDTOFromAccessTocken(String accessToken) {
        return UserInfo.builder()
                .providerId(this.getProviderId(accessToken))
                .nickName(this.getNickName(accessToken))
                .email(this.getEmail(accessToken))
                .profileImage(this.getProfileImage(accessToken))
                .build();
    }
*/

    public Claims getPayload(String token) {
        return jwtParser
                .parseSignedClaims(token)
                .getPayload();
    }

    public Long getMemberId(String token) {
        return getPayload(token).get("memberId", Long.class);
    }

    public String getProviderId(String token) {
        return getPayload(token).get("providerId", String.class);
    }

    public String getNickName(String token) {
        return getPayload(token).get("nickName", String.class);
    }

    public String getEmail(String token) {
        return getPayload(token).get("email", String.class);
    }

    public String getProfileImage(String token) {
        return getPayload(token).get("profileImage", String.class);
    }
}
