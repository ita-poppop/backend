package com.example.poppop.global.auth.service;

import com.example.poppop.global.auth.dto.TokenDto;
import com.example.poppop.global.auth.entity.RefreshToken;
import com.example.poppop.global.auth.repository.RefreshTokenRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;

    public void upsetRefreshTocken(TokenDto tokenDto) {
        refreshTokenRepository.findByEmail(tokenDto.getEmail())
                .ifPresentOrElse(
                        // 존재하면 업데이트
                        token -> token.updatePayload(tokenDto.getRefreshTocken()),
                        // 존재하지 않으면 생성
                        () -> this.create(tokenDto)
                );
    }

    private void create(TokenDto tockenDto) {
        refreshTokenRepository.save(new RefreshToken(tockenDto.getRefreshTocken(), tockenDto.getEmail()));
    }

    public void removeAllByEmail(String email) {
        refreshTokenRepository.deleteAllByEmail(email);
    }
}
