package com.example.demo;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;


@Entity
@Getter
public class Member extends BaseEntity {

    private String username;
    private String password;
    private String nickname;
    private String email;

    //authLevel

    @OneToMany(mappedBy = "member")
    private List<Product> productList = new ArrayList<>();
}
