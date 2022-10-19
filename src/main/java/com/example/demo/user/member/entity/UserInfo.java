//package com.example.demo.user.member.entity;
//
//import lombok.*;
//import lombok.experimental.SuperBuilder;
//import org.springframework.data.annotation.CreatedDate;
//import org.springframework.data.annotation.LastModifiedDate;
//import org.springframework.data.jpa.domain.support.AuditingEntityListener;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;
//
//import javax.persistence.*;
//import java.time.LocalDateTime;
//import java.util.*;
//
//
//@Entity
//@Getter
//@Setter
//@EntityListeners(AuditingEntityListener.class)
//@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
//@NoArgsConstructor(access = AccessLevel.PROTECTED)
//@AllArgsConstructor
//@DiscriminatorColumn
//@SuperBuilder
//public class UserInfo implements UserDetails {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column
//    private Long id;
//
//    @CreatedDate
//    private LocalDateTime createdAt;
//
//    @LastModifiedDate
//    private LocalDateTime updatedAt;
//
//
//    @Column(unique = true)
//    private String username;
//
//    private String password;
//    private String nickname;
//
//    @Column(name = "auth")
//    protected String auth;    // 권한(member, author, admin)
//
//    @Column(name = "email", unique = true)
//    private String email;
//
//
//    @Override
//    public Collection<? extends GrantedAuthority> getAuthorities() {
//        Set<GrantedAuthority> roles = new HashSet<>();
//        for (String role : auth.split(",")) {
//            roles.add(new SimpleGrantedAuthority(role));
//        }
//        return roles;
//    }
//
//    @Override
//    public boolean isAccountNonExpired() {
//        return true;
//    }
//
//    @Override
//    public boolean isAccountNonLocked() {
//        return true;
//    }
//
//    @Override
//    public boolean isCredentialsNonExpired() {
//        return true;
//    }
//
//    @Override
//    public boolean isEnabled() {
//        return true;
//    }
//
//    @Override
//    public String getUsername() {
//        return username;
//    }
//
//    @Override
//    public String getPassword() {
//        return password;
//    }
//
//
//    public String getEmail() {
//        return email;
//    }
//}
