package com.example.demo.article;

import com.example.demo.user.BaseEntity;
import com.example.demo.user.member.Member;
import lombok.Getter;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import static javax.persistence.FetchType.*;

@Entity
@Getter
public class PostHashTag extends BaseEntity {

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "postKeyword_id")
    private PostKeyword postKeyword;


    //index unique: postId + keywordId

}
