package com.clone.team4.domain.comment.repository;

import com.clone.team4.domain.comment.entity.Comment;
import com.clone.team4.domain.comment.entity.CommentLike;
import com.clone.team4.domain.user.entity.AccountInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
public interface CommentLikeRepository extends JpaRepository<CommentLike, Long> {
    Optional<CommentLike> findByCommentAndAccountInfo(Comment comment, AccountInfo accountInfo);
}
