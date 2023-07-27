package com.clone.team4.domain.post.repository;

import com.clone.team4.domain.post.entity.Post;
import com.clone.team4.domain.post.entity.QPost;
import com.clone.team4.domain.user.entity.AccountInfo;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

import static com.clone.team4.domain.post.entity.QPost.post;

@Repository
@RequiredArgsConstructor
public class QPostRepositoryImpl implements QPostRepository{

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Post> findPostsNotDeleted() {
        List<Post> posts = jpaQueryFactory.select(post)
                .from(post)
                .where(post.deletedAt.isNull())
                .orderBy(post.createdAt.desc())
                .fetch();

//        JPAQuery<Long> postsCount = getPostsCount();
//        return PageableExecutionUtils.getPage(posts, pageable, postsCount::fetchOne);
        return posts;
    }

    @Override
    public List<Post> findPostsByCategoryNotDeleted(String category) {
        List<Post> posts = jpaQueryFactory.select(post)
                .from(post)
                .where(post.category.eq(category))
                .where(post.deletedAt.isNull())
                .orderBy(post.createdAt.desc())
                .fetch();

//        JPAQuery<Long> postsCount = getPostsByCategoryCount(category);

//        return PageableExecutionUtils.getPage(posts, pageable, postsCount::fetchOne);
        return posts;
    }


    private JPAQuery<Long> getPostsCount() {
        return jpaQueryFactory
                .select(post.count())
                .from(post)
                .where(post.deletedAt.isNull());
    }

    private JPAQuery<Long> getPostsByCategoryCount(String category) {
        return jpaQueryFactory
                .select(post.count())
                .from(post)
                .where(post.category.eq(category))
                .where(post.deletedAt.isNull());
    }

    @Override
    public boolean isPostOwner(Long postId, AccountInfo accountInfo){
        return jpaQueryFactory.select(QPost.post.accountInfo.id)
            .from(post)
            .where(post.id.eq(postId).and(post.accountInfo.id.eq(accountInfo.getId())))
            .fetchFirst() != null;
    }

    @Override
    public void deletePost(Long postId, AccountInfo accountInfo){
        jpaQueryFactory.update(post)
            .set(post.deletedAt, LocalDateTime.now())
            .where(post.id.eq(postId), post.accountInfo.id.eq(accountInfo.getId()))
            .execute();
    }

}
