package com.clone.team4.domain.comment.dto;

import com.clone.team4.domain.comment.entity.Comment;
import lombok.Getter;

@Getter
public class CommentResponseDto {
    private String userImage;
    private String comment;
    private String nickname;

    public CommentResponseDto(Comment comment){
        this.userImage = comment.getAccountInfo().getProfileImage();
        this.comment = comment.getComment();
        this.nickname = comment.getAccountInfo().getNickname();
    }
}
