package com.example.demo.user.member.service;

import com.example.demo.user.member.entity.Member;
import com.example.demo.user.member.form.MemberModifyPasswordForm;
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
    public void create(String username, String email, String password, String nickname) {

        Member member = new Member();
        member.setUsername(username);
        member.setEmail(email);
        member.setPassword(passwordEncoder.encode(password));


        if(nickname == null || nickname.isEmpty()) {
            member.setAuth("ROLE_MEMBER");
        }
        else {
            member.setNickname(nickname);
            member.setAuth("ROLE_AUTHOR");
        }

        memberRepository.save(member);

    }

    public Member getMember(String username) {
        return memberRepository.findByUsername(username).orElseThrow(
                () -> new RuntimeException(username + "is not found."));
    }

    @Transactional
    public void modify(Member member, String email, String nickname) {

        if(nickname == null || nickname.isEmpty()) {
            if(member.getAuth().equals("ROLE_AUTHOR")) {
                member.setNickname(nickname);
            }
            member.setEmail(email);
        }
        else {
            member.setNickname(nickname);
            member.setAuth("ROLE_AUTHOR");
        }
    }


    @Transactional
    public void modifyPassword(Member member, String password) {
        member.setPassword(passwordEncoder.encode(password));
    }

    public boolean isSameOldPassword(Member member, MemberModifyPasswordForm memberModifyPasswordForm) {
        String encodedOldPassword = member.getPassword();
        String inputOldPassword = memberModifyPasswordForm.getOldPassword();

        return passwordEncoder.matches(inputOldPassword, encodedOldPassword);
    }

}
