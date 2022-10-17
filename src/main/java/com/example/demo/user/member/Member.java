package com.example.demo.user.member;


import com.example.demo.user.BaseEntity;
import com.example.demo.ebook.Product;
import com.example.demo.article.Post;
import com.example.demo.article.PostHashTag;
import lombok.Getter;

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


    @OneToMany(mappedBy = "member")
    private List<PostHashTag> postHashTagList = new ArrayList<>();


    @OneToMany(mappedBy = "member")
    private List<Post> postList = new ArrayList<>();

}
