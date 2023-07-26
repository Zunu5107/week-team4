package com.clone.team4.domain.comment.repository;

import com.clone.team4.domain.comment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
public interface CommentRepository extends JpaRepository<Comment, Long > {
    Optional<Comment> findByPostIdAndCommentId(Long PostId, Long CommentId);
}
