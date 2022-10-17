package com.example.demo;

import lombok.Getter;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class PostKeyword extends BaseEntity{

    private String content;

    @OneToMany(mappedBy = "postKeyword")
    private List<PostHashTag> postHashTagList = new ArrayList<>();

}
