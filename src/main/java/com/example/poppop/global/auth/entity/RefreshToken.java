package com.example.poppop.global.auth.entity;

import com.example.poppop.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RefreshToken extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String payload;

    @Column(nullable = false)
    private String email;

    @Builder
    public RefreshToken(String payload, String email) {
        this.payload = payload;
        this.email = email;
    }

    public RefreshToken updatePayload(String newPayload) {
        this.payload = newPayload;
        return this;
    }
}
