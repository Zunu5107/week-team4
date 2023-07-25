package com.clone.team4.domain.post.repository;

import com.clone.team4.domain.post.entity.PostDetails;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostDetailsRepository extends JpaRepository<PostDetails,Long>, QPostDetailsRepository {
    List<PostDetails> findAllByPostId(Long postId);

}
