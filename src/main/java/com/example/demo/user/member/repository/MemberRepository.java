package com.example.demo.user.member.repository;

import com.example.demo.user.member.entity.SiteMember;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<SiteMember, Long> {
    Optional<SiteMember> findByUsername(String username);
}
