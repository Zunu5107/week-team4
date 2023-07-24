package com.clone.team4.domain.like.repository;

import com.clone.team4.domain.like.entity.Like;

public interface QLikeRepository {
    Like findByPostIdAndAccountId(Long postId, Long accountId);
}
