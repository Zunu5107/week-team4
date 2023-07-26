package com.clone.team4.domain.post.dto;

import java.util.ArrayList;
import java.util.List;

import com.clone.team4.domain.post.entity.Post;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostInfoResponseDto {

    private Long postId;
    private String nickname;
    private String profileImage;
    private List<PostDetailsResponseDto> postDetails = new ArrayList<>();

    public PostInfoResponseDto(Post post, List<PostDetailsResponseDto> postDetails) {
        this.postId = post.getId();
        this.nickname = post.getAccountInfo().getNickname();
        this.profileImage = post.getAccountInfo().getProfileImage();
        this.postDetails = postDetails;
    }
}
