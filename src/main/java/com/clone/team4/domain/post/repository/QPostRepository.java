package com.clone.team4.domain.post.repository;

import com.clone.team4.domain.post.entity.Post;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface QPostRepository {
    Slice<Post> findPostsNotDeleted(Pageable pageable);

    Slice<Post> findPostsByCategoryNotDeleted(String category, Pageable pageable);
}
