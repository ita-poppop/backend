package com.example.poppop.global.auth.dto;

import com.example.poppop.domain.member.entity.Member;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TokenDto {
    private Long memberId;
    private String email;
    private String accessTocken;
    private String refreshTocken;

    @Builder
    public TokenDto(Long memberId, String email, String accessTocken, String refreshTocken) {
        this.memberId = memberId;
        this.email = email;
        this.accessTocken = accessTocken;
        this.refreshTocken = refreshTocken;
    }

    public static TokenDto of(Member member, String accessTocken, String refreshTocken) {
       return TokenDto.builder()
                .memberId(member.getId())
                .email(member.getEmail())
                .accessTocken(accessTocken)
                .refreshTocken(refreshTocken)
               .build();
    }
}
