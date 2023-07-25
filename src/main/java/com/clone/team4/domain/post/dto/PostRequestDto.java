package com.clone.team4.domain.post.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class PostRequestDto {

    private String content;

    public PostRequestDto() {
    }

    public PostRequestDto(String content) {
        this.content = content;
    }
}
