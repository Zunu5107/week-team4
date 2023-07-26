package com.clone.team4.domain.mypage.controller;

import com.clone.team4.domain.mypage.dto.MyPageResponseDto;
import com.clone.team4.domain.mypage.repository.MypageRepository;
import com.clone.team4.domain.mypage.service.MyPageService;
import com.clone.team4.global.dto.BaseResponseDto;
import com.clone.team4.global.exception.CustomStatusException;
import com.clone.team4.global.sercurity.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class MyPageController {

    private final MyPageService myPageService;
    @GetMapping("/mypage")
    public ResponseEntity<?> getMyPage(@AuthenticationPrincipal UserDetailsImpl userDetails){
        BaseResponseDto<?> responseDto = myPageService.getMyPage(userDetails.getAccountInfo());
        return ResponseEntity.status(Integer.parseInt(responseDto.getStatus())).body(responseDto);
    }

    @GetMapping("/{nickname}")
    public ResponseEntity<?> getMyPageForNickName(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                               @PathVariable String nickname){

        BaseResponseDto<?> responseDto = myPageService.getMyPageForNickName(nickname);
        return ResponseEntity.status(Integer.parseInt(responseDto.getStatus())).body(responseDto);
    }
}
