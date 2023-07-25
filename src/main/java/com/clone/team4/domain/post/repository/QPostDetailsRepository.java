package com.clone.team4.domain.post.repository;

import com.querydsl.core.Tuple;

public interface QPostDetailsRepository {
    Tuple findBySelectImageAndContentById(Long id);
}
