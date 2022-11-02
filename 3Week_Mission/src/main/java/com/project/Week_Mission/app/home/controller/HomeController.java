package com.project.Week_Mission.app.home.controller;

import com.project.Week_Mission.app.base.rq.Rq;
import com.project.Week_Mission.app.post.dto.PostDto;
import com.project.Week_Mission.app.post.entity.Post;
import com.project.Week_Mission.app.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class HomeController {
    private final PostService postService;
    private final Rq rq;

    @GetMapping("/")
    public String showMain(Model model) {
        if ( rq.isLogined() ) {
//            List<Post> posts = postService.findAllForPrintByAuthorIdOrderByIdDesc(rq.getId());
            List<PostDto> posts = postService.findAllForPrintByAuthorIdOrderByIdDesc(rq.getId());
            model.addAttribute("posts", posts);
        }

        return "home/main";
    }
}

