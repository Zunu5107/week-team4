package com.clone.team4.domain.post.service;

import java.util.ArrayList;
import java.util.List;

import com.clone.team4.domain.post.dto.PostDetailsResponseDto;
import com.clone.team4.domain.post.dto.PostInfoResponseDto;
import com.clone.team4.domain.post.dto.PostResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.clone.team4.domain.post.dto.PostRequestDto;
import com.clone.team4.domain.post.entity.Post;
import com.clone.team4.domain.post.entity.PostDetails;
import com.clone.team4.domain.post.image.ImageStore;
import com.clone.team4.domain.post.repository.PostDetailsRepository;
import com.clone.team4.domain.post.repository.PostRepository;
import com.clone.team4.domain.user.entity.AccountInfo;
import com.clone.team4.global.dto.BaseResponseDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final PostDetailsRepository postDetailsRepository;
    private final ImageStore imageStore;

    public BaseResponseDto<?> getPosts() {
        List<PostResponseDto> postResponseDtos = new ArrayList<>();
        List<Post> posts = postRepository.findAllByOrderByCreatedAtDesc();

        for (Post post : posts) {
            List<PostDetails> postDetails = postDetailsRepository.findTopByPostIdOrderByIdDesc(post.getId());

            for (PostDetails postDetail : postDetails) {
                postResponseDtos.add(new PostResponseDto(post, postDetail));
            }
        }

        BaseResponseDto<?> response = new BaseResponseDto<>(HttpStatus.OK.toString(), "게시글 전체 조회 성공", postResponseDtos);
        return response;
    }

    public BaseResponseDto<?> getPostById(Long postId) {
        BaseResponseDto<?> response = null;
        try {
            Post post = postRepository.findById(postId).orElseThrow(
                    () -> new IllegalArgumentException("해당 포스트가 없습니다."));

            List<PostDetailsResponseDto> postDetailsResponseDtos = postDetailsRepository.findAllByPostId(postId).stream()
                    .map(PostDetailsResponseDto::new).toList();

            PostInfoResponseDto data = new PostInfoResponseDto(post, postDetailsResponseDtos);

            response = new BaseResponseDto<>(HttpStatus.OK.toString(), "게시글 조회 성공", data);
            return response;
        } catch (IllegalArgumentException e) {
            response = new BaseResponseDto<>(HttpStatus.BAD_REQUEST.toString(), e.getMessage(), null);
            return response;
        }
    }

    // 게시글 생성
    // 게시글 생성 O
    // 이미지 리사이징 X
    // 리팩토링 X
    public BaseResponseDto<?> createPost(String category, List<MultipartFile> images, List<PostRequestDto> contentList,
        Integer imageCount, AccountInfo accountInfo) {
        if (!(imageCount > 0 && imageCount <= 10 && (images.size() == imageCount && contentList.size() == imageCount))) {
            throw new IllegalArgumentException("게시글 생성 오류");
        }
        List<String> imageUrls = imageStore.storeFile(images);

        Post post = new Post(category, 0L, accountInfo);
        Post savedPost = postRepository.save(post);
        List<PostDetails> postDetails = new ArrayList<>();
        Long postId = savedPost.getId();

        for (int i = 0; i<imageCount; i++) {
            Long postDetailsId = postId * 10 + i;
            postDetails.add(new PostDetails(postDetailsId, imageUrls.get(i), contentList.get(i).getContent(), savedPost));
        }

        postDetailsRepository.saveAll(postDetails);
        BaseResponseDto<?> response = new BaseResponseDto<>(HttpStatus.CREATED.toString(), "게시글 작성 성공", null);
        return response;
    }
}
