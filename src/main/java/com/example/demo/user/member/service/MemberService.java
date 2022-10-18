package com.example.demo.user.member.service;

import com.example.demo.user.member.entity.SiteUser;
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
    public SiteUser create(String username, String email, String password, String nickname) {

        SiteUser siteUser = new SiteUser();
        siteUser.setUsername(username);
        siteUser.setEmail(email);
//        member.setCreatedAt(LocalDateTime.now());
//        member.setAchieveLevel("USER");
        siteUser.setPassword(passwordEncoder.encode(password));
        siteUser.setNickname(nickname);

        memberRepository.save(siteUser);
        return siteUser;
    }

    public SiteUser getSiteUser(String username) {
        return memberRepository.findByUsername(username).orElseThrow(
                () -> new RuntimeException(username + "is not found."));
    }
}
