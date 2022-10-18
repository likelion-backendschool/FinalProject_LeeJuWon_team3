package com.example.demo.user.member.service;

import com.example.demo.user.member.entity.Member;
import com.example.demo.user.member.entity.SiteMember;
import com.example.demo.user.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;


    @Transactional
    public SiteMember create(String username, String email, String password) {

        SiteMember member = new SiteMember();
        member.setUsername(username);
        member.setEmail(email);
//        member.setCreatedAt(LocalDateTime.now());
//        member.setAchieveLevel("USER");
        member.setPassword(passwordEncoder.encode(password));

        memberRepository.save(member);
        return member;
    }

    public SiteMember getMember(String name) {
//        long l = Long.parseLong(name);
        return memberRepository.findByUsername(name).orElseThrow(
                () -> new RuntimeException(name + "is not found.")
        );
    }
}
