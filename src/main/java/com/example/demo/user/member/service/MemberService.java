package com.example.demo.user.member.service;

import com.example.demo.user.member.Member;
import com.example.demo.user.member.MemberRole;
import com.example.demo.user.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

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

}
