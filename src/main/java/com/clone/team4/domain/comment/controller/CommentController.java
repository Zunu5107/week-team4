package com.clone.team4.domain.comment.controller;

import com.clone.team4.domain.comment.dto.CommentRequestDto;
import com.clone.team4.domain.comment.service.CommentService;
import com.clone.team4.global.sercurity.UserDetailsImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/posts/{postId}/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping()
    public ResponseEntity<?> createComment(@PathVariable Long postId,
                                                            @RequestBody @Valid CommentRequestDto requestDto,
                                                            @AuthenticationPrincipal UserDetailsImpl userDetails){
        return commentService.createComment(postId, requestDto, userDetails);
    }

    @PutMapping("/{commentId}")
    public ResponseEntity<?> updateComment(@PathVariable Long postId,
                                                            @PathVariable Long commentId,
                                                            @RequestBody @Valid CommentRequestDto requestDto,
                                                            @AuthenticationPrincipal UserDetailsImpl userDetails){
        return  commentService.updateComment(postId, commentId, requestDto,userDetails);
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<?> deleteComment(@PathVariable Long postId,
                                                                 @PathVariable Long commentId,
                                                                 @AuthenticationPrincipal UserDetailsImpl userDetails){
        return commentService.deleteComment(postId, commentId, userDetails);
    }
}
