package com.project.Week_Mission.app.postkeyword.repository;

import com.project.Week_Mission.app.postkeyword.entity.PostKeyword;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

import static com.project.Week_Mission.app.postTag.entity.QPostTag.postTag;
import static com.project.Week_Mission.app.postkeyword.entity.QPostKeyword.postKeyword;


@RequiredArgsConstructor
public class PostKeywordRepositoryImpl implements PostKeywordRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<PostKeyword> getQslAllByAuthorId(Long authorId) {
        List<Tuple> fetch = jpaQueryFactory
                .select(postKeyword, postTag.count())
                .from(postKeyword)
                .innerJoin(postTag)
                .on(postKeyword.eq(postTag.postKeyword))
                .where(postTag.member.id.eq(authorId))
                .orderBy(postTag.post.id.desc())
                .groupBy(postKeyword.id)
                .fetch();

        return fetch.stream().
                map(tuple -> {
                    PostKeyword _postKeyword = tuple.get(postKeyword);
                    Long postTagsCount = tuple.get(postTag.count());

                    _postKeyword.getExtra().put("postTagsCount", postTagsCount);

                    return _postKeyword;
                })
                .collect(Collectors.toList());
    }
}
