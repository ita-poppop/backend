package com.example.poppop.domain.member.repository;

import com.example.poppop.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;


public interface MemberRepository extends JpaRepository<Member, Long> {

}
