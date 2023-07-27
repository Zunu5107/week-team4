package com.clone.team4.domain.post.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.clone.team4.domain.comment.dto.CommentResponseDto;
import com.clone.team4.domain.post.entity.Post;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostInfoResponseDto {

    private Long postId;
    private String nickname;
    private String profileImage;
    private LocalDateTime createdAt;
    private String category;
    private Long likeCount;
    private boolean isLike = false;
    private List<PostDetailsResponseDto> postDetails = new ArrayList<>();
    private List<CommentResponseDto> comments = new ArrayList<>();

    public PostInfoResponseDto(Post post,
                               List<PostDetailsResponseDto> postDetails,
                               List<CommentResponseDto> commentList) {
        this.postId = post.getId();
        this.nickname = post.getAccountInfo().getNickname();
        this.profileImage = post.getAccountInfo().getProfileImage();
        this.createdAt = post.getCreatedAt();
        this.category = post.getCategory();
        this.likeCount = post.getLikeCount();
        this.postDetails = postDetails;
        this.comments = commentList;
    }
}
