package com.clone.team4.domain.mypage.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MyPageResponseDto {
    String introduce;
    String userImage;
    String nickname;
    List<MyPagePostResponseDto> postList;
    List<MyPagePostResponseDto> likeList;
}
