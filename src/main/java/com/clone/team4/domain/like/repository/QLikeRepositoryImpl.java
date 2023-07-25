package com.clone.team4.domain.like.repository;

import com.clone.team4.domain.like.entity.Like;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static com.clone.team4.domain.like.entity.QLike.like;

@RequiredArgsConstructor
@Repository
public class QLikeRepositoryImpl implements QLikeRepository{

    private final JPAQueryFactory queryFactory;

    public Like findByPostIdAndAccountId(Long postId, Long accountId) {
        return queryFactory.select(like)
                .from(like)
                .where(like.post.id.eq(postId)
                    .and(like.accountInfo.id.eq(accountId)))
                .fetchFirst();
    }
}
