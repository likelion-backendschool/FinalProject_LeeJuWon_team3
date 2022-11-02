package com.project.Week_Mission.app.postTag.dto;

import com.project.Week_Mission.app.postTag.entity.PostTag;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class PostTagDto {

    private Long id;


    public PostTagDto(PostTag postTag) {
        id = postTag.getId();

    }
}
