package com.clone.team4.domain.like.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.clone.team4.domain.like.entity.Like;
import com.clone.team4.domain.like.repository.LikeRepository;
import com.clone.team4.domain.like.repository.QLikeRepository;
import com.clone.team4.domain.post.entity.Post;
import com.clone.team4.domain.post.repository.PostRepository;
import com.clone.team4.domain.user.entity.AccountInfo;
import com.clone.team4.domain.user.repository.AccountInfoRepository;
import com.clone.team4.global.dto.BaseResponseDto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class LikeService {

    private final LikeRepository likeRepository;
    private final PostRepository postRepository;
    private final AccountInfoRepository accountInfoRepository;
    private final QLikeRepository qLikeRepository;

    @Transactional
    public BaseResponseDto postLike(Long postId, AccountInfo accountInfo) {

        Post findPost = postRepository.findById(postId)
            .orElseThrow(() -> new IllegalArgumentException("해당 포스트가 없습니다."));

        AccountInfo findAccountInfo = accountInfoRepository.findById(accountInfo.getId())
            .orElseThrow(() -> new IllegalArgumentException("해당 사용자가 존재하지 않습니다."));

        Like findLike = qLikeRepository.findByPostIdAndAccountId(findPost.getId(),
            findAccountInfo.getId());

        Long likeCount = findPost.getLikeCount();

        if (findLike != null) {
            likeRepository.delete(findLike);
            findPost.updateLikeCount(--likeCount);
        } else {
            likeRepository.save(new Like(findAccountInfo, findPost));
            findPost.updateLikeCount(++likeCount);
        }

        BaseResponseDto response = BaseResponseDto.builder()
            .status(HttpStatus.OK.value())
            .msg("좋아요 성공")
            .build();

        return response;
    }
}
