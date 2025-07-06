package com.example.poppop.global.auth.service;

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

    public TokenDto createJwt(PopPopOAuth2User popPopOAuth2User) {
        String accessTocken=generateAccessTocken(popPopOAuth2User, new Date());
        String refreshTocken = generateRefreshTocken(popPopOAuth2User.getProviderId(), new Date());

        return TokenDto.builder()
                .email(popPopOAuth2User.getEmail())
                .accessTocken(accessTocken)
                .refreshTocken(refreshTocken)
                .build();
    }

    private String generateAccessTocken(PopPopOAuth2User user, Date now) {
        return Jwts.builder()
                .subject(user.getNickName())
                .claim("id", user.getProviderId())
                .claim("nickName", user.getNickName())
                .claim("email", user.getEmail())
                .claim("ProfileImage",user.getProfileImage())
                .issuedAt(new Date(now.getTime() + accessTockenExpiration))
                .signWith(secretKey)
                .compact();
    }

    private String generateRefreshTocken(String id, Date now) {
        return Jwts.builder()
                .subject(id)
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

    //JWT 토큰만으로 사용자 정보를 복원해야 하는 모든 상황에서 쓰임
    public UserInfo extractMemberDTOFromAccessTocken(String accessToken) {
        return UserInfo.builder()
                .providerId(this.getProviderId(accessToken))
                .nickName(this.getNickName(accessToken))
                .email(this.getEmail(accessToken))
                .profileImage(this.getProfileImage(accessToken))
                .build();
    }

    /**
     * 서명된 토큰 값을 파싱하여 payload를 추출
     *
     * public String getUsername(String token) {
     *         return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("username", String.class);
     *     }
     *     에서 반복되는 Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload()을 모듈화
     *
     * @param //token
     * @return claims(payload)
     */

    public Claims getPayload(String token) {
        return jwtParser
                .parseSignedClaims(token)
                .getPayload();
    }

    public String getProviderId(String token) {
        return getPayload(token).get("providerId", String.class);
    }

    public String getNickName(String token) {
        return getPayload(token).get("username", String.class);
    }

    public String getEmail(String token) {
        return getPayload(token).get("email", String.class);
    }

    public String getProfileImage(String token) {
        return getPayload(token).get("profileImage", String.class);
    }
}
