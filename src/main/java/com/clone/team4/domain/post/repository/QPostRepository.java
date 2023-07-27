package com.clone.team4.domain.post.repository;

import com.clone.team4.domain.post.entity.Post;
import com.clone.team4.domain.user.entity.AccountInfo;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface QPostRepository {
    List<Post> findPostsNotDeleted();

    List<Post> findPostsByCategoryNotDeleted(String category);

    boolean isPostOwner(Long postId, AccountInfo accountInfo);

    void deletePost(Long postId, AccountInfo accountInfo);
}
