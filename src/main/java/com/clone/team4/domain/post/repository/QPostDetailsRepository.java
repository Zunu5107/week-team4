package com.clone.team4.domain.post.repository;

import java.util.List;

import com.querydsl.core.Tuple;

public interface QPostDetailsRepository {
    Tuple findBySelectImageAndContentById(Long id);

    List<String> getPostImages(Long postId);
}
