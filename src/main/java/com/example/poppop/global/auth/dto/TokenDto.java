package com.example.poppop.global.auth.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TokenDto {
    private String email;
    private String accessTocken;
    private String refreshTocken;

    @Builder
    public TokenDto(String email, String accessTocken, String refreshTocken) {
        this.email = email;
        this.accessTocken = accessTocken;
        this.refreshTocken = refreshTocken;
    }
}
