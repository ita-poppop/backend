package com.example.poppop.global.auth.repository;

import com.example.poppop.global.auth.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByPayload(String payload);

    void deleteAllByEmail(String email);

    Optional<RefreshToken> findByEmail(String email);

    void deleteByMemberId(Long memberId);
}
