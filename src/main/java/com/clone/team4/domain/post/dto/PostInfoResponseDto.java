package com.clone.team4.domain.post.dto;

import com.clone.team4.domain.comment.dto.CommentResponseDto;
import com.clone.team4.domain.post.entity.Post;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

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
    private List<PostDetailsResponseDto> postDetails;
    private List<CommentResponseDto> comments;

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
