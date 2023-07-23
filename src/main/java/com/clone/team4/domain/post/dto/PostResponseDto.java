package com.clone.team4.domain.post.dto;

import com.clone.team4.domain.post.entity.Post;
import com.clone.team4.domain.post.entity.PostDetails;
import lombok.Getter;

@Getter
public class PostResponseDto {

    private Long postId;
    private String nickname;
    private String profileImage;
    private String postImage;
    private String content;

    public PostResponseDto(Post post, PostDetails postDetails) {
        this.postId = post.getId();
        this.nickname = post.getAccountInfo().getNickname();
        this.profileImage = post.getAccountInfo().getProfileImage();
        this.postImage = postDetails.getImage();
        this.content = postDetails.getContent();
    }
}