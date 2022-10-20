package com.example.demo.domain.member.form;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Getter
@Setter
public class MemberModifyForm {

    @Size(min = 3, max = 25)
    @NotEmpty(message = "사용자 이름은 필수항목입니다.")
    private String username;

    @NotEmpty(message = "이메일은 필수항목입니다.")
    @Email
    private String email;


    private String nickname;

}
