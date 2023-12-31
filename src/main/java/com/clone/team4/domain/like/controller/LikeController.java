package com.clone.team4.domain.like.controller;

import com.clone.team4.domain.like.service.LikeService;
import com.clone.team4.domain.post.exception.handler.PostExceptionHandler;
import com.clone.team4.global.dto.BaseResponseDto;
import com.clone.team4.global.sercurity.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api")
@RestController
public class LikeController implements PostExceptionHandler {

    private final LikeService likeService;

    @PatchMapping("/posts/{postId}/like")
    public ResponseEntity<BaseResponseDto> postLike(@PathVariable Long postId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        BaseResponseDto response = likeService.postLike(postId, userDetails.getAccountInfo());
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
