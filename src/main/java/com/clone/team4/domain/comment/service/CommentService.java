package com.clone.team4.domain.comment.service;


import com.clone.team4.domain.comment.dto.CommentRequestDto;
import com.clone.team4.domain.comment.dto.CommentResponseDto;
import com.clone.team4.domain.comment.entity.Comment;
import com.clone.team4.domain.comment.repository.CommentLikeRepository;
import com.clone.team4.domain.comment.repository.CommentRepository;
import com.clone.team4.domain.comment.entity.CommentLike;
import com.clone.team4.domain.post.entity.Post;
import com.clone.team4.domain.post.service.PostService;
import com.clone.team4.domain.user.entity.AccountInfo;
import com.clone.team4.global.dto.BaseResponseDto;
import com.clone.team4.global.sercurity.UserDetailsImpl;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final CommentLikeRepository commentLikeRepository;
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

    //<댓글 좋아요>
    public ResponseEntity<?> commentLike(Long postId, Long commentId, UserDetailsImpl userDetails) {
        // 댓글 유무 확인
        Comment targetComment = findComment(postId, commentId);
        AccountInfo targetAccountInfo = userDetails.getAccountInfo();
        CommentLike checkCommentLike = commentLikeRepository.findByCommentAndAccountInfo(targetComment, targetAccountInfo)
                .orElse(null);
        if (checkCommentLike == null) {
            CommentLike commentLike = new CommentLike(targetAccountInfo, targetComment);
            commentLikeRepository.save(commentLike);
            return ResponseEntity.ok("좋아요 성공");
        } else {
            commentLikeRepository.delete(checkCommentLike);
            return ResponseEntity.ok("좋아요 취소");
        }
    }

    //<댓글 찾기>
    public Comment findComment(Long postId, Long commentId){
        postService.findById(postId);
        return commentRepository.findByIdAndPostId(commentId, postId).orElseThrow(()->
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
}