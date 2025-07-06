package com.example.poppop.global.auth.dto;

import com.example.poppop.domain.member.entity.Member;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Schema(description = "로그인을 위한 필수 값 요청")
public class UserInfo {
    // OAuth2user에 담길 정보들
    // 인증에 관련된 최소 정보만 가지고 옴
    @Schema(description = "카카오 구글의 각 id")
    @NotNull
    private String providerId;

    @Schema(description = "카카오 구글의 각 registerId", example = "KAKAO, GOOGLE")
    @NotNull
    private String registerId;

    private String nickName;

    @NotNull
    private String email;

    private String profileImage;

    @Builder
    public UserInfo(String providerId, String registerId, String nickName, String email, String profileImage) {
        this.providerId = providerId;
        this.registerId = registerId;
        this.nickName = nickName;
        this.email = email;
        this.profileImage = profileImage;
    }

    public static UserInfo from(Member member) {
        return UserInfo.builder()
                .providerId(member.getProviderId())
                .registerId(member.getRegisterId())
                .nickName(member.getNickName())
                .profileImage(member.getProfileImage())
                .email(member.getEmail())
                .build();
    }
}
