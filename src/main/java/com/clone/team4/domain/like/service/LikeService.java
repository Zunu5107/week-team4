package com.clone.team4.domain.like.service;

import com.clone.team4.domain.like.entity.Like;
import com.clone.team4.domain.like.repository.LikeRepository;
import com.clone.team4.domain.post.entity.Post;
import com.clone.team4.domain.post.exception.PostNotFoundException;
import com.clone.team4.domain.post.repository.PostRepository;
import com.clone.team4.domain.user.entity.AccountInfo;
import com.clone.team4.global.dto.BaseResponseDto;
import com.clone.team4.global.exception.CustomStatusException;
import com.clone.team4.global.redis.AuthenticationRedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.clone.team4.global.redis.AuthenticationRedisService.AuthenticationStringEnum.LIKE;

@RequiredArgsConstructor
@Service
public class LikeService {

    private final LikeRepository likeRepository;
    private final PostRepository postRepository;
    private final AuthenticationRedisService authenticationRedisService;

    @Transactional
    public BaseResponseDto postLike(Long postId, AccountInfo accountInfo) {
        while (!authenticationRedisService.lock(LIKE.getAuthenticationString())){
            try {
                Thread.sleep(100);
            } catch (Exception e){
                throw CustomStatusException.builder("너무 많은 요청이 들어옴").status(HttpStatus.TOO_MANY_REQUESTS).build();
            }
        }
        try {
            Post findPost = postRepository.findById(postId)
                    .orElseThrow(() -> new PostNotFoundException("해당하는 포스트가 없습니다"));

            Like findLike = likeRepository.findByPostIdAndAccountId(findPost.getId(),
                    accountInfo.getId());

            if (findLike != null) {
                findPost.decreaseLikeCount();
                likeRepository.delete(findLike);
            } else {
                findPost.increaseLikeCount();
                likeRepository.save(new Like(accountInfo, findPost));
            }

            BaseResponseDto response = BaseResponseDto.builder()
                    .status(HttpStatus.OK.value())
                    .msg("좋아요 성공")
                    .build();

            return response;
        }
        finally {
            authenticationRedisService.unlock(LIKE.getAuthenticationString());
        }
    }
}
