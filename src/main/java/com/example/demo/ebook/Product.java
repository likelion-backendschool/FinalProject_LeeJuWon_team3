package com.example.demo.ebook;

import com.example.demo.post.entity.PostKeyword;
import com.example.demo.user.member.entity.SiteUser;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

import java.time.LocalDateTime;

import static javax.persistence.FetchType.*;

@Entity
@Getter
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Product {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    private String subjectName;
    private Long price;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "author_id")
    private SiteUser author;

    @OneToOne
    @JoinColumn(name = "postKeyword_id")
    private PostKeyword postKeyword;

}
