package com.example.demo.user.member.service;

import com.example.demo.user.member.entity.Member;
import com.example.demo.user.member.entity.SiteMember;
import com.example.demo.user.member.repository.MemberRepository;
import com.example.demo.user.member.role.MemberRole;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberSecurityService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        return memberRepository.findByUsername(username).map(this::createUserDetails)
//                .orElseThrow(() -> new UsernameNotFoundException(username + "is not Found."));

        SiteMember siteMember = memberRepository.findByUsername(username).orElseThrow(() ->
                new UsernameNotFoundException("사용자를 찾을수 없습니다.")
        );

        // 권한들을 담을 빈 리스트를 만든다.
        List<GrantedAuthority> authorities = new ArrayList<>();

        if ("admin".equals(username)) {
            authorities.add(new SimpleGrantedAuthority(MemberRole.ADMIN.getValue()));
        } else {
            authorities.add(new SimpleGrantedAuthority(MemberRole.USER.getValue()));
        }

        return new User(siteMember.getUsername(), siteMember.getPassword(), authorities);
    }

//    private UserDetails createUserDetails(Member member) {
//
//        String role = member.getAchieveLevel();
//
//        GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(role);
//
//        return new org.springframework.security.core.userdetails.User(
//                String.valueOf(member.getId()),
//                member.getPassword(),
//                Collections.singleton(grantedAuthority)
//        );
//    }
}
