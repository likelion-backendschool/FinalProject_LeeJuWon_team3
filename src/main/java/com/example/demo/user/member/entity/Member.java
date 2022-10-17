package com.example.demo.user.member.entity;

import com.example.demo.ebook.Product;
import com.example.demo.post.entity.Post;
import com.example.demo.post.entity.PostHashTag;
import com.example.demo.user.member.role.MemberRole;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Entity
@Getter @Setter
@EntityListeners(AuditingEntityListener.class)
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;


    @Column(unique = true)
    private String username;

    private String password;
    private String nickname;

    @Column(name = "achieveLevel")
    @Enumerated(EnumType.STRING)
    private MemberRole achieveLevel;

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