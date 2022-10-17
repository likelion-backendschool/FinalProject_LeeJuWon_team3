package com.example.demo.user.member.repository;

import com.example.demo.user.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {

}
