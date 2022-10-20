package com.example.demo.domain.member.entity;

import com.example.demo.domain.post.entity.Post;
import com.example.demo.domain.post.entity.PostHashTag;
import com.example.demo.domain.ebook.entity.Product;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@EntityListeners(AuditingEntityListener.class)
public class Member implements UserDetails {

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

    //TODO 삭제 예정
    @Column(name = "auth")
    protected String auth;    // 권한(member, author, admin)

    @Column(name = "email", unique = true)
    private String email;


    @Override
    public List<GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();

        authorities.add(new SimpleGrantedAuthority("MEMBER"));

        if(StringUtils.hasText(nickname)) {
            authorities.add(new SimpleGrantedAuthority("AUTHOR"));
        }

        return authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getPassword() {
        return password;
    }


    public String getEmail() {
        return email;
    }


    @OneToMany(mappedBy = "author")
    private List<Product> productList = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<PostHashTag> postHashTagList = new ArrayList<>();

    @OneToMany(mappedBy = "author")
    private List<Post> postList = new ArrayList<>();

}