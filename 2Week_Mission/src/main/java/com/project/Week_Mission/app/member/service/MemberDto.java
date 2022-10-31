package com.project.Week_Mission.app.member.service;

import com.project.Week_Mission.app.member.entity.Member;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter @Setter
public class MemberDto {

    private Long id;

    private String username;
    private String password;
    private String email;
    private boolean emailVerified;
    private long restCash;
    private String nickname;

    private LocalDateTime createDate;
    private LocalDateTime modifyDate;
//    private List<MyBookDto> myBookDtos;

    public MemberDto(Member member) {
        id = member.getId();
        createDate = member.getCreateDate();
        modifyDate = member.getModifyDate();
        username = member.getUsername();
        password = member.getPassword();
        email = member.getEmail();
        restCash = member.getRestCash();
        nickname = member.getNickname();
//        myBookDtos = member.getMyBooks().stream()
//                .map(myBookDtos -> new MyBookDto(myBookDtos))
//                .collect(Collectors.toList());
    }

}
