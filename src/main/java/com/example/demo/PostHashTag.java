package com.example.demo;

import lombok.Getter;

import javax.persistence.Entity;

@Entity
@Getter
public class PostHashTag extends BaseEntity {


    //index unique: postId + keywordId
}
