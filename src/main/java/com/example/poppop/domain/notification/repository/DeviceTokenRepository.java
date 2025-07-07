//package com.example.poppop.domain.notification.repository;
//
//import com.example.poppop.domain.member.entity.Member;
//import com.example.poppop.domain.notification.entity.DeviceToken;
//import org.springframework.data.jpa.repository.JpaRepository;
//
//import java.util.List;
//
//public interface DeviceTokenRepository extends JpaRepository<DeviceToken, Long> {
//
//    List<DeviceToken> findAllByMember(Member member);
//    boolean existsByMemberAndToken(Member member, String token);
//    void deleteByMemberAndToken(Member member, String token);
//}
