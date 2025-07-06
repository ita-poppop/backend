package com.example.poppop.global.auth.model;

import com.example.poppop.global.auth.dto.UserInfo;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

@ToString
@Getter
public class PopPopOAuth2User implements UserDetails {
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

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // 일반 유저 권한을 "ROLE_USER"로 지정
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
    }
    @Override
    public String getPassword() { return null; }
    @Override
    public String getUsername() { return email; }
    @Override
    public boolean isAccountNonExpired() { return true; }
    @Override
    public boolean isAccountNonLocked() { return true; }
    @Override
    public boolean isCredentialsNonExpired() { return true; }
    @Override
    public boolean isEnabled() { return true; }
}
