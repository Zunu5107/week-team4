package com.clone.team4.domain.post.controller;

import com.clone.team4.domain.post.service.PostService;
import com.clone.team4.global.dto.BaseResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j(topic = "post controller")
public class PostController {

    private final PostService postService;

    @GetMapping("/posts")
    public ResponseEntity<BaseResponseDto> getPosts(){
        log.info("getPosts");
        return null;
    }
//    @GetMapping("/posts")
//    public ResponseEntity<BaseResponseDto> getPostsByCategory(@RequestParam String category){
//        log.info("with category");
//        return null;
//    }

    @PostMapping("/posts")
    public ResponseEntity<BaseResponseDto> createPost() {
        return null;
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
