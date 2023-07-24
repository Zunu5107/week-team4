package com.clone.team4.domain.post.repository;

import com.clone.team4.domain.post.entity.Post;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long>{
    Slice<Post> findAllByOrderByCreatedAtDesc(Pageable pageable);

    Slice<Post> findAllByCategoryOrderByCreatedAtDesc(String category, Pageable pageable);
}
