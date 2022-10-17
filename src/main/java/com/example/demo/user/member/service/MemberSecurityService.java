package com.example.demo.user.member.service;

import com.example.demo.user.member.entity.Member;
import com.example.demo.user.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class MemberSecurityService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return memberRepository.findByUsername(username).map(this::createUserDetails)
                .orElseThrow(() -> new UsernameNotFoundException(username + "is not Found."));
    }

    private UserDetails createUserDetails(Member member) {

        String role = member.getAchieveLevel().getValue();

        GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(role);

        return new org.springframework.security.core.userdetails.User(
                String.valueOf(member.getId()),
                member.getPassword(),
                Collections.singleton(grantedAuthority)
        );
    }
}
