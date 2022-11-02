package com.project.Week_Mission.app.post.service;

import com.project.Week_Mission.app.base.rq.Rq;
import com.project.Week_Mission.app.member.entity.Member;
import com.project.Week_Mission.app.post.dto.PostDto;
import com.project.Week_Mission.app.post.entity.Post;
import com.project.Week_Mission.app.post.repository.PostRepository;
import com.project.Week_Mission.app.postTag.entity.PostTag;
import com.project.Week_Mission.app.postTag.service.PostTagService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {
    private final PostRepository postRepository;
    private final PostTagService postTagService;

    @Transactional
    public PostDto write(Member author, String subject, String content, String contentHtml, String postTagContents) {
        Post post = Post.builder()
                .subject(subject)
                .content(content)
                .contentHtml(contentHtml)
                .author(author)
                .build();
        postRepository.save(post);

        applyPostTags(post, postTagContents);

        return new PostDto(post);
    }

    public PostDto findById(long postId) {
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new RuntimeException(postId + " postId is not found."));
        return new PostDto(post);
    }

    public void applyPostTags(Post post, String postTagContents) {
        postTagService.applyPostTags(post, postTagContents);
    }

    public List<PostTag> getPostTags(Post post) {
        return postTagService.getPostTags(post);
    }

    @Transactional
    public void modify(PostDto postDto, String subject, String content, String contentHtml, String postTagContents) {

        Post post = postRepository.findById(postDto.getId()).orElseThrow(
                () -> new RuntimeException(postDto.getId() + " postDtoId is not found."));

        post.updateSubject(subject);
        post.updateContent(content);
        post.updateContentHtml(contentHtml);

        applyPostTags(post, postTagContents);
    }

    public boolean actorCanModify(Member author, PostDto postDto) {
        return author.getId().equals(postDto.getAuthor().getId());
    }

    public boolean actorCanRemove(Member author, PostDto postDto) {
        return actorCanModify(author, postDto);
    }

    public List<PostTag> getPostTags(Member author, String postKeywordContent) {
        List<PostTag> postTags = postTagService.getPostTags(author, postKeywordContent);

        loadForPrintDataOnPostTagList(postTags);

        return postTags;
    }

    @Transactional
    public PostDto findForPrintById(long id) {

        Post post = postRepository.findById(id).orElseThrow(() -> new RuntimeException(id + "postId is not found."));

        List<PostTag> postTags = getPostTags(post);

        post.getExtra().put("postTags", postTags);

        return new PostDto(post);
    }


    private void loadForPrintDataOnPostTagList(List<PostTag> postTags) {
        List<Post> posts = postTags
                .stream()
                .map(PostTag::getPost)
                .collect(toList());

        loadForPrintData(posts);
    }


    @Transactional
    public void loadForPrintData(List<Post> posts) {
        long[] ids = posts
                .stream()
                .mapToLong(Post::getId)
                .toArray();

        List<PostTag> postTagsByPostIds = postTagService.getPostTagsByPostIdIn(ids);

        Map<Long, List<PostTag>> postTagsByPostIdsMap = postTagsByPostIds.stream()
                .collect(groupingBy(
                        postTag -> postTag.getPost().getId(), toList()
                ));

        posts.stream().forEach(post -> {
            List<PostTag> postTags = postTagsByPostIdsMap.get(post.getId());

            if (postTags == null || postTags.size() == 0) return;

            post.getExtra().put("postTags", postTags);
        });
    }

    public List<PostDto> findAllForPrintByAuthorIdOrderByIdDesc(long authorId) {
        List<Post> posts = postRepository.findAllByAuthorIdOrderByIdDesc(authorId);
        loadForPrintData(posts);

        return posts.stream()
                .map(o -> new PostDto(o))
                .collect(toList());
    }

    @Transactional
    public void remove(PostDto postDto) {
        Post post = postRepository.findById(postDto.getId())
                .orElseThrow(
                        () -> new RuntimeException(postDto.getId() + " is not found."));

        postRepository.delete(post);
    }

    public boolean actorCanSee(Member actor, Post post) {
        if ( actor == null ) return false;
        if ( post == null ) return false;

        return post.getAuthor().getId().equals(actor.getId());
    }
}
