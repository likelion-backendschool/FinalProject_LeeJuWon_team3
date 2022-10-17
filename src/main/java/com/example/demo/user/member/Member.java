package com.example.demo.user.member;


import com.example.demo.user.BaseEntity;
import com.example.demo.ebook.Product;
import com.example.demo.post.Post;
import com.example.demo.post.PostHashTag;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;


@Entity
@Getter @Setter
public class Member extends BaseEntity {

    private String username;

    private String password;
    private String nickname;


    @Column(unique = true)
    private String email;

    //authLevel

    @OneToMany(mappedBy = "member")
    private List<Product> productList = new ArrayList<>();


    @OneToMany(mappedBy = "member")
    private List<PostHashTag> postHashTagList = new ArrayList<>();


    @OneToMany(mappedBy = "member")
    private List<Post> postList = new ArrayList<>();

}
