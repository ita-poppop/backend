package com.example.poppop.domain.member.dto;

import com.example.poppop.domain.member.entity.Member;
import lombok.Builder;
import lombok.Getter;

@Getter
public class MemberResponse {
    private final Long id;
    private final String providerId;
    private final String registerId;
    private final String email;
    private final String nickName;
    private final String profileImage;

    @Builder
    public MemberResponse(Long id, String providerId, String registerId, String email, String nickName, String profileImage) {
        this.id = id;
        this.providerId = providerId;
        this.registerId = registerId;
        this.email = email;
        this.nickName = nickName;
        this.profileImage = profileImage;
    }

    // Member 엔티티에서 DTO로 변환하는 편의 메서드
    public static MemberResponse from(Member member) {
        return MemberResponse.builder()
                .id(member.getId())
                .providerId(member.getProviderId())
                .registerId(member.getRegisterId())
                .email(member.getEmail())
                .nickName(member.getNickName())
                .profileImage(member.getProfileImage())
                .build();
    }

}
