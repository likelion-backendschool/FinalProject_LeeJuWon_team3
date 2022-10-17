package com.example.demo;

import lombok.Getter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.*;

@Entity
@Getter
public class Post extends BaseEntity {


    private String subjectTitle;

    private String content;

    //contentHtml

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "post_id")
    private Member member;

    @OneToMany(mappedBy = "post")
    private List<PostHashTag> postHashTagList = new ArrayList<>();


}
