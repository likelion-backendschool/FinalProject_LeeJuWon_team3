package com.project.Week_Mission.app.post.controller;

import com.project.Week_Mission.app.base.exception.ActorCanNotModifyException;
import com.project.Week_Mission.app.base.exception.ActorCanNotRemoveException;
import com.project.Week_Mission.app.base.rq.Rq;
import com.project.Week_Mission.app.member.entity.Member;
import com.project.Week_Mission.app.post.dto.PostDto;
import com.project.Week_Mission.app.post.entity.Post;
import com.project.Week_Mission.app.post.form.PostForm;
import com.project.Week_Mission.app.post.service.PostService;
import com.project.Week_Mission.app.postTag.entity.PostTag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping("/post")
@Slf4j
public class PostController {
    private final PostService postService;
    private final Rq rq;

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/write")
    public String showWrite() {
        return "post/write";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/write")
    public String write(@Valid PostForm postForm) {
        Member author = rq.getMember();
        PostDto post = postService.write(author, postForm.getSubject(), postForm.getContent(), postForm.getContentHtml(), postForm.getPostTagContents());
        return Rq.redirectWithMsg("/post/" + post.getId(), "%d번 글이 생성되었습니다.".formatted(post.getId()));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}/modify")
    public String showModify(@PathVariable long id, Model model) {
        PostDto post = postService.findForPrintById(id);
        Member actor = rq.getMember();

        if (postService.actorCanModify(actor, post) == false) {
            throw new ActorCanNotModifyException();
        }

        model.addAttribute("post", post);

        return "post/modify";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/{id}/modify")
    public String modify(@Valid PostForm postForm, @PathVariable long id) {
        PostDto post = postService.findById(id);
        Member actor = rq.getMember();

        if (postService.actorCanModify(actor, post) == false) {
            throw new ActorCanNotModifyException();
        }

        postService.modify(post, postForm.getSubject(), postForm.getContent(), postForm.getContentHtml(), postForm.getPostTagContents());
        return Rq.redirectWithMsg("/post/" + post.getId(), "%d번 글이 수정되었습니다.".formatted(post.getId()));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model) {
        PostDto post = postService.findForPrintById(id);
        Member actor = rq.getMember();

        if (postService.actorCanModify(actor, post) == false) {
            throw new ActorCanNotModifyException();
        }

        model.addAttribute("post", post);

        return "post/detail";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/list")
    public String list(Model model) {
        List<PostDto> posts = postService.findAllForPrintByAuthorIdOrderByIdDesc(rq.getId());

        model.addAttribute("posts", posts);

        return "post/list";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/tag/{tagContent}")
    public String tagList(Model model, @PathVariable String tagContent) {
        List<PostTag> postTags = postService.getPostTags(rq.getMember(), tagContent);

        model.addAttribute("postTags", postTags);
        return "post/tagList";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/{id}/remove")
    public String remove(@PathVariable long id) {
        PostDto post = postService.findById(id);
        Member actor = rq.getMember();

        if (postService.actorCanRemove(actor, post) == false) {
            throw new ActorCanNotRemoveException();
        }

        postService.remove(post);

        return Rq.redirectWithMsg("/post/list", "%d번 글이 삭제되었습니다.".formatted(post.getId()));
    }
}
