package com.example.demo.user.member.service;

import com.example.demo.user.member.entity.Member;
import com.example.demo.user.member.role.MemberRole;
import com.example.demo.user.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;


    @Transactional
    public Member create(String username, String email, String password) {

        Member member = new Member();
        member.setUsername(username);
        member.setEmail(email);
        member.setCreatedAt(LocalDateTime.now());
        member.setAchieveLevel(MemberRole.USER);
        member.setPassword(passwordEncoder.encode(password));

        memberRepository.save(member);
        return member;
    }

    public Member findById(String name) {
        long l = Long.parseLong(name);
        return memberRepository.findById(l).orElseThrow(
                () -> new RuntimeException(name + "is not found.")
        );
    }
}
