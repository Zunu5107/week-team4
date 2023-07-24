package com.clone.team4.domain.like.repository;

import static com.clone.team4.domain.like.entity.QLike.*;

import org.springframework.stereotype.Repository;

import com.clone.team4.domain.like.entity.Like;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Repository
public class QLikeRepository {

    private final JPAQueryFactory queryFactory;

    public Like findByPostIdAndAccountId(Long postId, Long accountId) {
        return queryFactory.select(like)
                .from(like)
                .where(like.post.id.eq(postId)
                    .and(like.accountInfo.id.eq(accountId)))
                .fetchFirst();
    }
}
