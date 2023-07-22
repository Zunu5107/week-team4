package com.clone.team4.domain.post.repository;

import com.clone.team4.domain.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long>{
    List<Post> findAllByOrderByCreatedAtDesc();
}
