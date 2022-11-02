package com.project.Week_Mission.app.post.dto;


import com.project.Week_Mission.app.member.entity.Member;
import com.project.Week_Mission.app.post.entity.Post;
import com.project.Week_Mission.app.postTag.dto.PostTagDto;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter @Setter
public class PostDto {

    private Long id;

    private LocalDateTime createDate;

    private LocalDateTime modifyDate;
    private String subject;
    private String content;
    private String contentHtml;
//    private String postTagContents;

//    private List<PostTagDto> postTags;
    private Member author;
    private String authorName;
    private String jdenticon;
    private String authorJdenticon;
    private String extra_postTagLinks;

    private String forPrintContentHtml;
    public PostDto(Post post) {
        id = post.getId();
        createDate = post.getCreateDate();
        modifyDate = post.getModifyDate();
        subject = post.getSubject();
        content = post.getContent();
        contentHtml = post.getContentHtml();
        author = post.getAuthor();
        authorName = post.getAuthor().getName();
        jdenticon = post.getJdenticon();
        authorJdenticon = post.getAuthor().getJdenticon();
        extra_postTagLinks = post.getExtra_postTagLinks();
        forPrintContentHtml = post.getForPrintContentHtml();

//        TODO: null일 수도 있는 값은 dto에 넣으면 안되나?
//        postTags = post.getPostTags()
//                .stream()
//                .map(o -> new PostTagDto(o))
//                .collect(Collectors.toList());
    }
}
