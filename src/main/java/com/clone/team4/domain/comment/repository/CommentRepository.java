package com.clone.team4.domain.comment.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.clone.team4.domain.comment.entity.Comment;
public interface CommentRepository extends JpaRepository<Comment, Long > {
    Optional<Comment> findByPostIdAndCommentId(Long PostId, Long CommentId);
}
