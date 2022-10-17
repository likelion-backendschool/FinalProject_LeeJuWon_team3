package com.example.demo;

import lombok.Getter;

import javax.persistence.Entity;

@Entity
@Getter
public class Product extends BaseEntity{


    private String subjectName;
    private Long price;
}
