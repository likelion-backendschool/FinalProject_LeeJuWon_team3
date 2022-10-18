package com.example.demo.user.member.form;

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

    @NotEmpty(message = "닉네임은 필수항목입니다.")
    @Size(min = 1, max = 25)
    private String nickname;

}
