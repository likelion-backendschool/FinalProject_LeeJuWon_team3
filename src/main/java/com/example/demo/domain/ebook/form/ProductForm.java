package com.example.demo.domain.ebook.form;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter @Setter
public class ProductForm {

    @NotEmpty(message="상품(도서) 이름은 필수항목입니다.")
    private String subject;

    private int price;

//    private PostKeyword postKeyword;

}
