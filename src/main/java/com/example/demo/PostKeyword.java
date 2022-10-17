package com.example.demo;

import lombok.Getter;

import javax.persistence.Entity;

@Entity
@Getter
public class PostKeyword extends BaseEntity{


    private String content;
}
