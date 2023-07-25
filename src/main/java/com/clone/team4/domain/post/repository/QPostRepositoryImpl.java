package com.clone.team4.domain.post.repository;

import com.clone.team4.domain.post.entity.Post;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.clone.team4.domain.post.entity.QPost.post;

@Repository
@RequiredArgsConstructor
public class QPostRepositoryImpl implements QPostRepository{

    private final JPAQueryFactory queryFactory;

    @Override
    public Slice<Post> findPostsNotDeleted(Pageable pageable) {
        List<Post> posts = queryFactory.select(post)
                .from(post)
                .where(post.deletedAt.isNull())
                .orderBy(post.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> postsCount = getPostsCount();
        return PageableExecutionUtils.getPage(posts, pageable, postsCount::fetchOne);
    }

    @Override
    public Slice<Post> findPostsByCategoryNotDeleted(String category, Pageable pageable) {
        List<Post> posts = queryFactory.select(post)
                .from(post)
                .where(post.category.eq(category))
                .where(post.deletedAt.isNull())
                .orderBy(post.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> postsCount = getPostsByCategoryCount(category);

        return PageableExecutionUtils.getPage(posts, pageable, postsCount::fetchOne);
    }


    private JPAQuery<Long> getPostsCount() {
        return queryFactory
                .select(post.count())
                .from(post)
                .where(post.deletedAt.isNull());
    }

    private JPAQuery<Long> getPostsByCategoryCount(String category) {
        return queryFactory
                .select(post.count())
                .from(post)
                .where(post.category.eq(category))
                .where(post.deletedAt.isNull());
    }
}
