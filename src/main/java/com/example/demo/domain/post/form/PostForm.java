package com.example.demo.domain.post.form;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Getter @Setter
public class PostForm {

    @NotEmpty(message="제목은 필수항목입니다.")
    @Size(max = 200)
    private String subject;


    @NotEmpty(message = "내용은 최소 1000자 이상입니다.")
    @Size(min = 1000)
    private String content;

}
