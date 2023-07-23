package com.clone.team4.domain.post.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.clone.team4.domain.post.dto.PostRequestDto;
import com.clone.team4.domain.post.service.PostService;
import com.clone.team4.global.dto.BaseResponseDto;
import com.clone.team4.global.sercurity.UserDetailsImpl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j(topic = "post controller")
public class PostController {

    private final PostService postService;

    @GetMapping("/posts")
    public ResponseEntity<BaseResponseDto> getPosts(){
        log.info("getPosts");
        return ResponseEntity.status(HttpStatus.CREATED).body(new BaseResponseDto("201","success",null));
    }
//    @GetMapping("/posts")
//    public ResponseEntity<BaseResponseDto> getPostsByCategory(@RequestParam String category){
//        log.info("with category");
//        return null;
//    }

    @PostMapping(value = "/posts")
    public ResponseEntity<BaseResponseDto> createPost(@RequestPart("category") String category,
        @RequestPart("image") List<MultipartFile> imageList,
        @RequestPart("content") List<PostRequestDto> contentList,
        @RequestParam("imageCount") Integer imageCount,
        @AuthenticationPrincipal UserDetailsImpl userDetails) {

        BaseResponseDto<?> response = postService.createPost(category, imageList, contentList, imageCount,
            userDetails.getAccountInfo());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/posts/{postId}")
    public ResponseEntity<BaseResponseDto> updatePost() {
        return null;
    }

    @DeleteMapping("/posts/{postId}")
    public ResponseEntity<BaseResponseDto> deletePost() {
        return null;
    }
}
