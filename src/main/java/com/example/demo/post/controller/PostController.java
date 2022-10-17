package com.example.demo.post.controller;

import com.example.demo.post.Post;
import com.example.demo.post.PostForm;
import com.example.demo.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/post")
public class PostController {

    private final PostService postService;

    /**
     * 글  리스트
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
     * 글 등록
     * - 폼 입력
     *     - subject
     *     - content
     *     - keywords
     *         - 입력예시
     *             #자바 #스프링부트 #스프링배치
     */
    @GetMapping("/write")
    public String postWrite() {
        return "posts/post_form";
    }


    @PostMapping("/write")
    public String postWrite(
            @Valid PostForm postForm,
            BindingResult bindingResult
//            @RequestParam String keywords
    ) {

        if( bindingResult.hasErrors()) {
            return "posts/post_form";
        }

        postService.write(postForm.getSubject(),
                postForm.getContent());

        return "redirect:/post/list"; //글 저장후 글목록으로 이동
    }

}
