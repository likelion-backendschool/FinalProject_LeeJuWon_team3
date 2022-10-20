package com.example.demo.domain.post.controller;

import com.example.demo.domain.member.entity.Member;
import com.example.demo.domain.post.entity.Post;
import com.example.demo.domain.post.form.PostForm;
import com.example.demo.domain.post.service.PostService;
import com.example.demo.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/post")
public class PostController {

    private final PostService postService;
    private final MemberService memberService;


    /**
     * 글 리스트
     * 글 리스트, 전체 노출
     * 리스트 아이템 구성요소: 번호, 제목, 작성자, 작성날짜, 수정날짜
     */
    @GetMapping("/list")
    public String postList(Model model) {

        List<Post> postList = postService.findPosts();
        model.addAttribute("postList", postList);

        return "posts/post_list";
    }



    /**
     * 글 상세
     * - 번호, 제목, 작성자, 작성날짜, 수정날짜, 내용
     * - 해시태그
     */
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}")
    public String postDetail(
            Model model,
            @PathVariable("id") Long id) {

        Post post = postService.getPost(id);

        model.addAttribute("post", post);

        return "posts/post_detail";
    }


    /**
     * 글 등록
     * - 폼 입력
     *     - subject
     *     - content
     *     - keywords
     *         - 입력예시
     *             #자바 #스프링부트 #스프링배치
     */
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/write")
    public String postWrite(PostForm postForm) {
        return "posts/post_form";
    }


    @PreAuthorize("isAuthenticated()")
    @PostMapping("/write")
    public String postWrite(
            @Valid PostForm postForm,
            BindingResult bindingResult,
            Principal principal) {

        if( bindingResult.hasErrors()) {
            return "posts/post_form";
        }

        Member member = memberService.findMember(principal.getName());


        try {
            postService.write(postForm.getSubject(), postForm.getContent(), member);

        } catch (Exception e) {
            e.printStackTrace();
            bindingResult.reject("writeFailed", e.getMessage());
            return "posts/post_form";
        }


        return "redirect:/post/list"; //글 등록 후 글목록으로 이동
    }



    /**
     * 글 수정
     * - 폼 입력
     *     - subject
     *     - content
     *     - postKeywordContents
     *         - 입력예시
     *
     *             #자바 #스프링부트
     */
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}/modify")
    public String postModify(
            PostForm postForm,
            @PathVariable("id") Long id,
            Principal principal) {
        Post post = postService.getPost(id);

//        if ( post == null ) {
//            throw new DataNotFoundException("%d번 질문은 존재하지 않습니다.");
//        }

        if(!post.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }

        postForm.setSubject(post.getSubject());
        postForm.setContent(post.getContent());

        return "posts/post_form";
    }


    @PreAuthorize("isAuthenticated()")
    @PostMapping("/{id}/modify")
    public String postModify(
            @Valid PostForm postForm,
            BindingResult bindingResult,
            @PathVariable("id") Long id,
            Principal principal) {

        if(bindingResult.hasErrors()) {
            return "posts/post_form";
        }

        Post post = postService.getPost(id);

        if(!post.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }

        try {
            postService.modify(post, postForm.getSubject(), postForm.getContent());
        } catch (Exception e) {
            e.printStackTrace();
            bindingResult.reject("modifyFailed", e.getMessage());
            return "posts/post_form";
        }


        return String.format("redirect:/post/%s", id);
    }


    /**
     * 글 삭제
     */
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}/delete")
    public String postDelete(
            Principal principal,
            @PathVariable("id") Long id) {

        Post post = postService.getPost(id);


        if (!post.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제권한이 없습니다.");
        }

        postService.delete(post);

        return "redirect:/";
    }

}
