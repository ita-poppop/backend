package com.example.poppop.domain.member.entity;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.Map;

@Getter
public class CustomOAuth2User implements org.springframework.security.oauth2.core.user.OAuth2User {
    private final CustomOAuth2User oauth2UserCustom;

    public CustomOAuth2User(CustomOAuth2User oauth2UserCustom) {
        this.oauth2UserCustom = oauth2UserCustom;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return oauth2UserCustom.getAttributes();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return oauth2UserCustom.getAuthorities();
    }

    @Override
    public String getName() {
        return oauth2UserCustom.getName();
    }

    // 원하는 사용자 식별자 getter 추가
    public Long getId() {
        // provider별로 id attribute가 다를 수 있음에 유의!
        Object idAttr = oauth2UserCustom.getAttribute("id");
        if (idAttr == null) {
            idAttr = oauth2UserCustom.getAttribute("sub"); // 예: 구글은 "sub"
        }
        return idAttr != null ? Long.valueOf(idAttr.toString()) : null;
    }

    public String getEmail() {
        return oauth2UserCustom.getAttribute("email");
    }
}
