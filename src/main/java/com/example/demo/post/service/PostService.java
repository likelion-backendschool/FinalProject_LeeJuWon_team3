package com.example.demo.post.service;

import com.example.demo.post.entity.Post;
import com.example.demo.post.repository.PostRepository;
import com.example.demo.user.member.entity.Member;
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
    public void write(String subject, String content, Member author) {

        Post post = new Post();
        post.setSubject(subject);
        post.setContent(content);
        post.setAuthor(author);
        post.setCreatedAt(LocalDateTime.now());
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
        postRepository.save(post);
    }


    @Transactional
    public void delete(Post post) {
        postRepository.delete(post);
    }
}
