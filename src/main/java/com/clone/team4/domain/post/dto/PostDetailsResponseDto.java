package com.clone.team4.domain.post.dto;

import com.clone.team4.domain.post.entity.PostDetails;
import lombok.Getter;

@Getter
public class PostDetailsResponseDto {
    private String postImage;
    private String content;

    public PostDetailsResponseDto(PostDetails postDetails) {
        this.postImage = postDetails.getImage();
        this.content = postDetails.getContent();
    }
}
