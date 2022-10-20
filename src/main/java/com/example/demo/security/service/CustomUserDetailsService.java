package com.example.demo.security.service;

import com.example.demo.domain.member.repository.MemberRepository;
import com.example.demo.domain.member.role.MemberRole;
import com.example.demo.domain.member.entity.Member;
import com.example.demo.security.dto.MemberContext;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을수 없습니다."));


        return new MemberContext(member, member.getAuthorities());

//        List<GrantedAuthority> authorities = new ArrayList<>();

//        if ("admin".equals(username)) {
//            authorities.add(new SimpleGrantedAuthority(MemberRole.ADMIN.getValue()));
//        } else {
//            authorities.add(new SimpleGrantedAuthority(MemberRole.USER.getValue()));
//        }
//
//        return new User(member.getUsername(), member.getPassword(), authorities);
    }




}
