package com.clone.team4.domain.mypage.controller;

import com.clone.team4.domain.mypage.dto.MyPageResponseDto;
import com.clone.team4.domain.mypage.repository.MypageRepository;
import com.clone.team4.global.dto.BaseResponseDto;
import com.clone.team4.global.sercurity.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class MyPageController {

    private final MypageRepository mypageRepository;
    @GetMapping("/mypage")
    public ResponseEntity getMyPage(@AuthenticationPrincipal UserDetailsImpl userDetails){

        MyPageResponseDto result = new MyPageResponseDto();
        result.setIntroduce(userDetails.getAccountInfo().getIntroduce());
        result.setNickname(userDetails.getAccountInfo().getNickname());
        result.setUserImage(userDetails.getAccountInfo().getProfileImage());
        result.setPostList(mypageRepository.findByMyPost(userDetails.getAccountInfo().getId()));
        result.setPostList(mypageRepository.findByMyLikePost(userDetails.getAccountInfo().getId()));
        BaseResponseDto responseDto = BaseResponseDto.builder()
                .msg("success")
                .status(200)
                .data(result)
                .build();

        return ResponseEntity.ok(responseDto);
    }

    @GetMapping("/{nickname}")
    public ResponseEntity getMyPageForNickName(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                               @PathVariable String nickname){
        MyPageResponseDto result = mypageRepository.findByMypageByAccountNickName(nickname);
        BaseResponseDto responseDto = BaseResponseDto.builder()
                .msg("success")
                .status(200)
                .data(result)
                .build();
        return ResponseEntity.ok(responseDto);
    }
}
