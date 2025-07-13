package com.example.poppop.global.auth.dto;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Getter
public class PopPopUserDetails implements UserDetails {
    private final String providerId;
    private final String registId;
    private final String email;
    private final String nickName;
    private final String profileImage;

    public PopPopUserDetails(UserInfo userInfo) {
        this.providerId = userInfo.getProviderId();
        this.registId=userInfo.getRegisterId();
        this.email = userInfo.getEmail();
        this.nickName = userInfo.getNickName();
        this.profileImage = userInfo.getProfileImage();
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
