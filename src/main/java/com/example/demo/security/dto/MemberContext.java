package com.example.demo.security.dto;


import com.example.demo.domain.member.entity.Member;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class MemberContext extends User {

    private final Long id;
    private final LocalDateTime createAt;
    private final LocalDateTime updatedAT;
    private final String username;
    private final String password;
    private final String email;
    private final String nickname;

    public MemberContext(Member member, List<GrantedAuthority> authorities) {
        super(member.getUsername(), member.getPassword(), authorities);
        this.id = member.getId();
        this.createAt = member.getCreatedAt();
        this.updatedAT = member.getUpdatedAt();
        this.username = member.getUsername();
        this.password = member.getPassword();
        this.email = member.getEmail();
        this.nickname = member.getNickname();
    }

}
