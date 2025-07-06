package com.example.poppop.global.auth.model;

import com.example.poppop.global.auth.dto.UserInfo;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class PopPopOAuth2User {
    private String providerId;
    private String registerId;
    private String nickName;
    private String email;
    private String profileImage;

    @Builder
    public PopPopOAuth2User(String providerId,String registerId, String nickName, String email, String profileImage) {
        this.providerId = providerId;
        this.registerId = registerId;
        this.nickName = nickName;
        this.email = email;
        this.profileImage = profileImage;
    }

    public static PopPopOAuth2User from(UserInfo userInfo) {
        return PopPopOAuth2User.builder()
                .providerId(userInfo.getProviderId())
                .registerId(userInfo.getRegisterId())
                .nickName(userInfo.getNickName())
                .email(userInfo.getEmail())
                .profileImage(userInfo.getProfileImage())
                .build();
    }
}
