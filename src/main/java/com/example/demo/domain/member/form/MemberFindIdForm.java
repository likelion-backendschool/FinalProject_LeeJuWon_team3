package com.example.demo.domain.member.form;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Getter @Setter
public class MemberFindIdForm {

    @NotEmpty(message = "이메일은 필수항목입니다.")
    private String email;

}
