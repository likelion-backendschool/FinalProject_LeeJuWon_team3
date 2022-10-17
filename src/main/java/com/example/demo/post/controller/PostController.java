package com.example.demo.post.controller;

import com.example.demo.post.Post;
import com.example.demo.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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

        return "posts/postList";
    }
}
