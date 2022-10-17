package com.example.demo;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;


@Entity
@Getter
public class Member extends BaseEntity {

    private String username;
    private String password;
    private String nickname;
    private String email;

    //authLevel
}
