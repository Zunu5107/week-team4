package com.clone.team4.domain.post.controller;

import com.clone.team4.domain.post.dto.PostRequestDto;
import com.clone.team4.domain.post.service.PostService;
import com.clone.team4.global.dto.BaseResponseDto;
import com.clone.team4.global.sercurity.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j(topic = "post controller")
public class PostController {

    private final PostService postService;

    @GetMapping("/posts")
    public ResponseEntity<BaseResponseDto> getPosts(){
        BaseResponseDto<?> response = postService.getPosts();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    //    @GetMapping("/posts")
//    public ResponseEntity<BaseResponseDto> getPostsByCategory(@RequestParam String category){
//        log.info("with category");
//        return null;
//    }
    @GetMapping("/posts/{postId}")
    public ResponseEntity<BaseResponseDto> getPostById(@PathVariable Long postId) {
        BaseResponseDto<?> response = postService.getPostById(postId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping(value = "/posts")
    public ResponseEntity<BaseResponseDto> createPost(@RequestPart("category") String category,
                                                      @RequestPart("image") List<MultipartFile> imageList,
                                                      @RequestPart("content") List<PostRequestDto> contentList,
                                                      @RequestParam("imageCount") Integer imageCount,
                                                      @AuthenticationPrincipal UserDetailsImpl userDetails) {

        log.info("createPost Controller Running ={}", imageList.get(1).getOriginalFilename());
        log.info("category = {}", category);
        log.info("content.size() = {}", contentList.size());
        log.info("imageTotalCount = {}", imageCount);
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
