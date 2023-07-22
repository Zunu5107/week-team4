package com.clone.team4.domain.post.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.clone.team4.domain.post.entity.PostDetails;

import java.util.List;

public interface PostDetailsRepository extends JpaRepository<PostDetails,Long> {
    List<PostDetails> findTopByPostIdOrderByIdDesc(Long postId);

    List<PostDetails> findAllByPostId(Long postId);
}
