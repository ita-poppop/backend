//package com.example.poppop.domain.notification.entity;
//
//import com.example.poppop.domain.member.entity.Member;
//import jakarta.persistence.*;
//import lombok.AccessLevel;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//
//@Entity
//@Table(name = "device_token", uniqueConstraints = @UniqueConstraint(columnNames = {"member_id","token"}))
//@Getter
//@NoArgsConstructor(access = AccessLevel.PROTECTED)
//public class DeviceToken {
//
//    @Id @GeneratedValue
//    private Long id;
//
//    @ManyToOne
//    @JoinColumn(name="member_id")
//    private Member member;
//
//    @Column(nullable = false)
//    private String token;
//
//    public DeviceToken(Member member, String token) {
//        this.member = member;
//        this.token  = token;
//    }
//}
