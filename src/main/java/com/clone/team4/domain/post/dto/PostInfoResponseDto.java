package com.clone.team4.domain.post.dto;

import com.clone.team4.domain.post.entity.Post;
import com.clone.team4.domain.post.entity.PostDetails;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

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