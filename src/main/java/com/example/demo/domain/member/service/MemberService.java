package com.example.demo.domain.member.service;

import com.example.demo.domain.member.entity.Member;
import com.example.demo.domain.member.repository.MemberRepository;
import com.example.demo.domain.member.form.MemberModifyPasswordForm;
import com.example.demo.security.dto.MemberContext;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    public void create(String username, String email, String password, String nickname) {

        Member member = Member.builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .email(email)
                .nickname(nickname)
                .build();

        memberRepository.save(member);
    }

    public Member findMember(String username) {
        return memberRepository.findByUsername(username).orElseThrow(
                () -> new RuntimeException(username + "is not found."));
    }

    @Transactional
    public void modify(String username, String email, String nickname) {

        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException(username + "is not found."));

        member.setEmail(email);
        member.setUpdatedAt(LocalDateTime.now());

        //TODO nickname 등록 다르게 구현
//        if(nickname == null || nickname.isEmpty()) {
//            if(member.getAuth().equals("ROLE_AUTHOR")) {
//                member.setNickname(nickname);
//            }
//        }
//        else {
//            member.setNickname(nickname);
//            member.setAuth("ROLE_AUTHOR");
//        }

    }


    @Transactional
    public void modifyPassword(String username, String password) {
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException(
                username + "is not found."));

        member.setUpdatedAt(LocalDateTime.now());
        member.setPassword(passwordEncoder.encode(password));
    }

    public boolean isSameOldPassword(@AuthenticationPrincipal MemberContext memberContext, MemberModifyPasswordForm memberModifyPasswordForm) {

        String encodedOldPassword = memberContext.getPassword();
        String inputOldPassword = memberModifyPasswordForm.getOldPassword();

        return passwordEncoder.matches(inputOldPassword, encodedOldPassword);
    }

    public Member findByEmail(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(
                        () -> new RuntimeException(email + " is not Found")
                );
    }
}
