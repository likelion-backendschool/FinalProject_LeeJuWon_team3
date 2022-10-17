package com.example.demo;

import lombok.Getter;

import javax.persistence.Entity;

@Entity
@Getter
public class Post extends BaseEntity {


    private String subjectTitle;
    private String content;

    //contentHtml
}
