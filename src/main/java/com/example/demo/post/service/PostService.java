package com.example.demo.post.service;

import com.example.demo.post.Post;
import com.example.demo.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    public List<Post> findPosts() {
        return postRepository.findAll();
    }

    public void write(String subject, String content, String keywords) {

        Post post = new Post();
        post.setSubjectTitle(subject);
        post.setContent(content);
        post.setKeywords(keywords);
        post.setCreatedAt(LocalDateTime.now());
        postRepository.save(post);
    }
}
