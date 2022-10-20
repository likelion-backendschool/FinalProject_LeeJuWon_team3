package com.example.demo.domain.post.service;

import com.example.demo.domain.member.entity.Member;
import com.example.demo.domain.post.entity.Post;
import com.example.demo.domain.post.repository.PostRepository;
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

    @Transactional
    public void write(String subject, String content, Member member) {

        Post post = Post.builder()
                .subject(subject)
                .content(content)
                .author(member)
                .build();

        postRepository.save(post);
    }

    public Post getPost(Long id) {

        Post post = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post id" + id + "not found"));

        return post;
    }

    @Transactional
    public void modify(Post post, String subject, String content) {
        post.setSubject(subject);
        post.setContent(content);
        post.setUpdatedAt(LocalDateTime.now());
    }


    @Transactional
    public void delete(Post post) {
        postRepository.delete(post);
    }
}
