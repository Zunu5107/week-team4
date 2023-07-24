package com.clone.team4.domain.post.service;

import com.clone.team4.domain.post.dto.PostDetailsResponseDto;
import com.clone.team4.domain.post.dto.PostInfoResponseDto;
import com.clone.team4.domain.post.dto.PostRequestDto;
import com.clone.team4.domain.post.dto.PostResponseDto;
import com.clone.team4.domain.post.entity.Post;
import com.clone.team4.domain.post.entity.PostDetails;
import com.clone.team4.domain.post.image.S3ImageUploader;
import com.clone.team4.domain.post.repository.PostDetailsRepository;
import com.clone.team4.domain.post.repository.PostRepository;
import com.clone.team4.domain.user.entity.AccountInfo;
import com.clone.team4.domain.user.entity.UserRoleEnum;
import com.clone.team4.global.dto.BaseResponseDto;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.clone.team4.domain.post.entity.QPost.post;
import static com.clone.team4.domain.post.entity.QPostDetails.postDetails;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final PostDetailsRepository postDetailsRepository;
    private final S3ImageUploader s3ImageUploader;
    private final PostServiceHelper postServiceHelper;

    private final JPAQueryFactory jpaQueryFactory;

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
    @Transactional
    public BaseResponseDto<?> createPost(String category, List<MultipartFile> imageList,
        List<PostRequestDto> contentList, Integer imageCount, AccountInfo accountInfo) {

        postServiceHelper.validPostCreateRequest(imageList, contentList, imageCount, category);
        List<String> imageUrls = s3ImageUploader.storeFile(imageList);
        Post post = new Post(category, accountInfo, imageCount);
        Post savedPost = postRepository.save(post);

        List<PostDetails> postDetailsList = PostDetails.createPostDetailsList(imageUrls, contentList, savedPost, postServiceHelper.getMAX_IMAGE_COUNT());
        postDetailsRepository.saveAll(postDetailsList);

        BaseResponseDto<?> response = new BaseResponseDto<>(HttpStatus.CREATED.toString(), "게시글 작성 성공", null);
        return response;
    }

    @Transactional
    public BaseResponseDto<?> updatePost(Long postId, String category, List<MultipartFile> imageList, List<PostRequestDto> contentList,
        Integer imageCount, AccountInfo accountInfo) {

        postServiceHelper.validPostCreateRequest(imageList,contentList,imageCount, category);

        Post savedPost = postRepository.findById(postId)
            .orElseThrow(() -> new IllegalArgumentException("해당 포스트가 없습니다."));

        if (!postServiceHelper.hasRole(accountInfo,savedPost)){
            throw new IllegalArgumentException("권한이 없습니다.");
        }

        List<String> savedImages = jpaQueryFactory
            .select(postDetails.image)
            .from(postDetails)
            .where(postDetails.post.id.eq(savedPost.getId()))
            .fetch();

        s3ImageUploader.deleteFile(savedImages);

        savedPost.updatePost(category, imageCount);

        List<String> newImageUrls = s3ImageUploader.storeFile(imageList);

        List<PostDetails> newPostDetails = PostDetails.createPostDetailsList(newImageUrls, contentList, savedPost, postServiceHelper.getMAX_IMAGE_COUNT());
        postDetailsRepository.saveAll(newPostDetails);

        BaseResponseDto<?> response = new BaseResponseDto<>(HttpStatus.OK.toString(), "게시글 수정 성공", null);
        return response;
    }

    @Transactional
    public BaseResponseDto deletePost(Long postId, AccountInfo accountInfo){
        Long postAccountId = jpaQueryFactory.select(post.accountInfo.id)
            .from(post)
            .where(post.id.eq(postId).and(post.accountInfo.id.eq(accountInfo.getId())))
            .fetchFirst();

        if (!(postAccountId != null || accountInfo.getRole().equals(UserRoleEnum.ADMIN)))
            throw new IllegalArgumentException("권한이 없습니다.");

        jpaQueryFactory.update(post).set(post.deletedAt, LocalDateTime.now()).where(post.id.eq(postId), post.accountInfo.id.eq(accountInfo.getId())).execute();
        return new BaseResponseDto(HttpStatus.OK.toString(), "게시글 삭제 성공",null);
    }
}
