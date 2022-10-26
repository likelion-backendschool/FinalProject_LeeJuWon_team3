package com.project.Week_Mission.app.member.service;

import com.project.Week_Mission.app.mybook.MyBook;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
public class MyBookDto {

    private Long id;

    private Long memberId;
    private Long productId;
    private LocalDateTime createDate;
    private LocalDateTime modifyDate;

    public MyBookDto(MyBook myBook) {
        id = myBook.getId();
        memberId = myBook.getMember().getId();
        productId = myBook.getProduct().getId();
        createDate = myBook.getCreateDate();
        modifyDate = myBook.getModifyDate();
    }

}
