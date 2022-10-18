package com.example.demo.user.member.service;

import com.example.demo.user.member.entity.SiteUser;
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

        SiteUser siteUser = new SiteUser();
        siteUser.setUsername(username);
        siteUser.setEmail(email);
        siteUser.setPassword(passwordEncoder.encode(password));


        if(nickname == null || nickname.isEmpty()) {
            siteUser.setAuth("ROLE_MEMBER");
        }
        else {
            siteUser.setNickname(nickname);
            siteUser.setAuth("ROLE_AUTHOR");
        }

        memberRepository.save(siteUser);

    }

    public SiteUser getSiteUser(String username) {
        return memberRepository.findByUsername(username).orElseThrow(
                () -> new RuntimeException(username + "is not found."));
    }

    @Transactional
    public void modify(SiteUser siteUser, String email, String nickname) {

        if(nickname == null || nickname.isEmpty()) {
            if(siteUser.getAuth().equals("ROLE_AUTHOR")) {
                siteUser.setNickname(nickname);
            }
            siteUser.setEmail(email);
        }
        else {
            siteUser.setNickname(nickname);
            siteUser.setAuth("ROLE_AUTHOR");
        }
    }


    @Transactional
    public void modifyPassword(SiteUser siteUser, String password) {
        siteUser.setPassword(passwordEncoder.encode(password));
    }

    public boolean isSameOldPassword(SiteUser siteUser, MemberModifyPasswordForm memberModifyPasswordForm) {
        String encodedOldPassword = siteUser.getPassword();
        String inputOldPassword = memberModifyPasswordForm.getOldPassword();

        return passwordEncoder.matches(inputOldPassword, encodedOldPassword);
    }

}
