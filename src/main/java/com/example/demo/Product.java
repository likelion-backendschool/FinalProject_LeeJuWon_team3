package com.example.demo;

import lombok.Getter;

import javax.persistence.*;

import static javax.persistence.FetchType.*;

@Entity
@Getter
public class Product extends BaseEntity{


    private String subjectName;
    private Long price;


    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member member;


    @OneToOne
    @JoinColumn(name = "postKeyword_id")
    private PostKeyword postKeyword;

}
