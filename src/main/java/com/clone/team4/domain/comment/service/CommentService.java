package com.clone.team4.domain.comment.service;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.stereotype.Service;

import com.clone.team4.domain.comment.dto.CommentRequestDto;
import com.clone.team4.domain.comment.entity.Comment;
import com.clone.team4.domain.comment.repository.CommentRepository;
import com.clone.team4.domain.post.entity.Post;
import com.clone.team4.domain.post.service.PostService;
import com.clone.team4.domain.user.entity.AccountInfo;
import com.clone.team4.global.dto.BaseResponseDto;
import com.clone.team4.global.sercurity.UserDetailsImpl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostService postService;

    public ResponseEntity<?> createComment(Long postId, CommentRequestDto requestDto, UserDetailsImpl userDetails){
        AccountInfo targetAccountInfo = userDetails.getAccountInfo();
        Post targetPost = postService.findById(postId);
        if(targetPost == null)
            return ResponseEntity.noContent().build();
        Comment comment = new Comment(requestDto, targetPost, targetAccountInfo);
        commentRepository.save(comment);
        BaseResponseDto<?> response = new BaseResponseDto<>(HttpStatus.CREATED.toString(), "댓글 작성 성공", null);
        return ResponseEntity.ok(response);
    }

    //<댓글 수정>
    @Transactional
    public ResponseEntity<?> updateComment(Long postId, Long commentId, CommentRequestDto requestDto, UserDetailsImpl userDetails){
        Comment targetComment = findComment(postId, commentId);
        AccountInfo targetAccountInfo = userDetails.getAccountInfo();
        checkAuthority(targetComment, targetAccountInfo );
        targetComment.update(requestDto);
        BaseResponseDto<?> response = new BaseResponseDto<>(HttpStatus.OK.toString(), "댓글 수정 성공", null);
        return ResponseEntity.ok(response);
    }

    //<댓글 삭제>
    public ResponseEntity<?> deleteComment(Long postId, Long commentId, UserDetailsImpl userDetails){
        Comment targetComment = findComment(postId, commentId);
        AccountInfo targetAccountInfo = userDetails.getAccountInfo();
        checkAuthority(targetComment, targetAccountInfo );
        commentRepository.delete(targetComment);
        BaseResponseDto<?> response = new BaseResponseDto<>(HttpStatus.OK.toString(), "댓글 삭제 성공", null);
        return  ResponseEntity.ok(response);
    }

//<댓글 찾기>
    public Comment findComment(Long postId, Long commentId){
        postService.findById(postId);
        return commentRepository.findByPostIdAndCommentId(postId, commentId).orElseThrow(()->
                new NullPointerException("댓글이 존재하지 않습니다."));
    }

//<댓글 수정 권한 확인>
    public void checkAuthority(Comment comment, AccountInfo accountInfo){
        if(!accountInfo.getRole().getAuthority().equals("ROLE_ADMIN")){
            if(!comment.getAccountInfo().getId().equals(accountInfo.getId())){
                throw new AuthorizationServiceException("수정 권한이 없습니다.");
            }
        }
    }

//        //<댓글 수정>
//    Comment targetComment = findComment(postId, commentId);
//        if(targetComment.getAccountInfo().getId() != userDetails.getAccountInfo().getId()) // comment id가 같지 않으면
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED.value()).build(); // 인증되지 않았다고 돌려줌
//        // 아니면 여기부터 다시 진행
//        Comment comment = new Comment(requestDto, targetPost, accountInfo);
//        return ResponseEntity.ok(new CommentResponseDto());
}
