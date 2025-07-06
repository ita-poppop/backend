package com.example.poppop.domain.member.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
@Schema(description = "로그인을 위한 필수 값 요청")
public class MemberRequest {
    @Schema(description = "카카오 구글의 각 id")
    @NotNull
    private String id;

    @Schema(description = "카카오 구글의 각 registerId", example = "KAKAO, GOOGLE")
    @NotNull
    private String registerId;

    private String nickName;

    @NotNull
    private String email;

    private String profileImage;
}
