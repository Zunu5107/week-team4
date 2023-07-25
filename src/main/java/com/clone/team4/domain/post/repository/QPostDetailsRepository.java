package com.clone.team4.domain.post.repository;

import com.querydsl.core.Tuple;

import java.util.List;

public interface QPostDetailsRepository {
    Tuple findBySelectImageAndContentById(Long id);

    List<String> getPostImages(Long postId);
}
