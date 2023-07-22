package com.clone.team4.domain.post.service;

import java.util.ArrayList;
import java.util.List;

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
