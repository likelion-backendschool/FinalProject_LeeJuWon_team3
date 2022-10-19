package com.example.demo.ebook.form;

import com.example.demo.post.entity.PostKeyword;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Getter @Setter
public class ProductForm {

    @NotEmpty(message="상품(도서) 이름은 필수항목입니다.")
    private String subject;

    private int price;

//    private PostKeyword postKeyword;

}
