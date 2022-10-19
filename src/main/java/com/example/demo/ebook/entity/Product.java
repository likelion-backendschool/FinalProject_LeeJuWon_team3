package com.example.demo.ebook.entity;

import com.example.demo.post.entity.PostKeyword;
import com.example.demo.user.member.entity.Member;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

import java.time.LocalDateTime;

import static javax.persistence.FetchType.*;

@Entity
@Getter @Setter
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
public class Product {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @CreatedDate
    private LocalDateTime createdAt;

//    @LastModifiedDate
//    private LocalDateTime updatedAt;

    private String subject;
    private int price;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "author_id")
    private Member author;

    @OneToOne
    @JoinColumn(name = "postKeyword_id")
    private PostKeyword postKeyword;

}
