package com.clone.team4.domain.comment.dto;


import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class CommentRequestDto {
    @NotEmpty(message = "255자 내로 댓글을 입력해주세요.")
    @Size(min =1, max = 255, message = "255자 내로 댓글을 입력해주세요.")
    private String comment;
}
