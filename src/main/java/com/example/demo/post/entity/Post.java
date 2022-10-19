package com.example.demo.post.entity;

import com.example.demo.user.member.entity.Member;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
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


    @Column(columnDefinition = "TEXT", length = 10485760)
    private String content;


    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "author_id")
    private Member author;


    //contentHtml 내용(토스트에디터의 렌더링 결과, HTML)


    @OneToMany(mappedBy = "post")
    private List<PostHashTag> postHashTagList = new ArrayList<>();


}
