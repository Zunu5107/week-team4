package com.clone.team4.domain.post.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.clone.team4.domain.post.entity.PostDetails;

public interface PostDetailsRepository extends JpaRepository<PostDetails,Long> {
    List<PostDetails> findTopByPostIdOrderByIdDesc(Long postId);

    List<PostDetails> findAllByPostId(Long postId);

}
