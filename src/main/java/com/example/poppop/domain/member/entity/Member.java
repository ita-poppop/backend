package com.example.poppop.domain.member.entity;

import lombok.Getter;

import com.example.poppop.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "member",
        uniqueConstraints = @UniqueConstraint(name = "uk_member_email", columnNames = "email"),
        indexes = @Index(name = "idx_member_email", columnList = "email"))
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "profile_url", length = 50)
    private String profileUrl;

    @Column(length = 50, nullable = false)
    private String email;

    @Column(name = "user_name", nullable = false)
    private String userName;

    @Column(name = "notification_set", nullable = false)
    private Boolean notificationSet = Boolean.TRUE;

    private String providerId;

    private String registerId;

    private String nickName;

    private String profileImage;

    @Builder
    private Member(String profileUrl,
                   String email,
                   String userName,
                   Boolean notificationSet,
                   String providerId,
                   String registerId,
                   String nickName,
                   String profileImage
                   ) {

        this.profileUrl      = profileUrl;
        this.email           = email;
        this.userName        = userName;
        this.notificationSet = (notificationSet != null) ? notificationSet : Boolean.TRUE;
        this.providerId     = providerId;
        this.registerId      = registerId;
        this.nickName      = nickName;
        this.profileImage    = profileImage;
    }

    public void updateProfile(String newUrl, String newName) {
        this.profileUrl = newUrl;
        this.userName   = newName;
    }

    public void changeNotification(boolean enabled) {
        this.notificationSet = enabled;
    }
}

