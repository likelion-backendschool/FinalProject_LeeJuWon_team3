package com.example.demo.domain.member.form;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Getter @Setter
public class MemberModifyPasswordForm {

    @Size(min = 3, max = 25)
    @NotEmpty(message = "사용자 이름은 필수항목입니다.")
    private String username;

    @NotEmpty(message = "이전 비밀번호는 필수항목입니다.")
    private String oldPassword;

    @NotEmpty(message = "새로운 비밀번호는 필수항목입니다.")
    private String password;

    @NotEmpty(message = "새로운 비밀번호 확인은 필수항목입니다.")
    private String passwordConfirm;
}
