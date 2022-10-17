package com.example.demo.post;

import com.example.demo.user.member.Member;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.*;

@Entity
@Getter @Setter
@EntityListeners(AuditingEntityListener.class)
public class Post {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    private String subject;

    private String content;

    //contentHtml

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "post_id")
    private Member member;

    @OneToMany(mappedBy = "post")
    private List<PostHashTag> postHashTagList = new ArrayList<>();


}
