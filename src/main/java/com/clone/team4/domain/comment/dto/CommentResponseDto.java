package com.clone.team4.domain.comment.dto;


import com.clone.team4.domain.comment.entity.Comment;
import com.clone.team4.domain.user.entity.AccountInfo;
import lombok.Getter;


import java.util.List;
//import java.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
public class CommentResponseDto {
    private Long commentId;
    private String comment;
    private String nickname;
    private String createdAt;
    private String modifiedat;
    private List<String> LikesList;

    public CommentResponseDto(Comment comment){
        this.commentId = comment.getCommentId();
        this.comment = comment.getComment();
        this.nickname = comment.getNickname();
        this.createdAt = comment.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        this.modifiedat = comment.getModifiedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
    }
}
