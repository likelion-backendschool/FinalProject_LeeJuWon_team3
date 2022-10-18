package com.example.demo.user.member.entity;

import com.example.demo.ebook.Product;
import com.example.demo.post.entity.Post;
import com.example.demo.post.entity.PostHashTag;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.*;


@Entity
@Getter @Setter
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

//    @Column(name = "achieveLevel")
//    @Enumerated(EnumType.STRING)
//    private MemberRole achieveLevel;

    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "achieveLevel")
    protected String achieveLevel; // ADMIN, USER

    //authLevel

    @OneToMany(mappedBy = "member")
    private List<Product> productList = new ArrayList<>();


    @OneToMany(mappedBy = "member")
    private List<PostHashTag> postHashTagList = new ArrayList<>();


//    @OneToMany(mappedBy = "member")
//    private List<Post> postList = new ArrayList<>();


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<GrantedAuthority> roles = new HashSet<>();
        for (String role : achieveLevel.split(",")) {
            roles.add(new SimpleGrantedAuthority(role));
        }
        return roles;
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

}
