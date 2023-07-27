package com.clone.team4.domain.post.service;

import com.amazonaws.util.StringUtils;
import com.clone.team4.domain.comment.dto.CommentResponseDto;
import com.clone.team4.domain.comment.entity.Comment;
import com.clone.team4.domain.comment.repository.CommentRepository;
import com.clone.team4.domain.like.entity.Like;
import com.clone.team4.domain.like.repository.LikeRepository;
import com.clone.team4.domain.post.dto.*;
import com.clone.team4.domain.post.entity.Post;
import com.clone.team4.domain.post.entity.PostDetails;
import com.clone.team4.domain.post.exception.PostNotFoundException;
import com.clone.team4.domain.post.repository.PostDetailsRepository;
import com.clone.team4.domain.post.repository.PostRepository;
import com.clone.team4.domain.user.entity.AccountInfo;
import com.clone.team4.domain.user.entity.User;
import com.clone.team4.global.dto.BaseResponseDto;
import com.clone.team4.global.exception.PermissionDeniedException;
import com.clone.team4.global.image.ImageFolderEnum;
import com.clone.team4.global.image.S3ImageUploader;
import com.clone.team4.global.sercurity.UserDetailsImpl;
import com.querydsl.core.Tuple;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static com.clone.team4.domain.post.entity.QPostDetails.postDetails;
@Slf4j(topic = "PostService")
@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final PostDetailsRepository postDetailsRepository;
    private final CommentRepository commentRepository;
    private final LikeRepository likeRepository;
    private final S3ImageUploader s3ImageUploader;
    private final PostServiceHelper postServiceHelper;

    @Transactional(readOnly = true)
    public PageDto<?> getPosts(PageParam pageParam) {
        log.info("page param = {}, {}, {}", pageParam.getPage(), pageParam.getSize(), pageParam.getCategory());
        Pageable pageable = PageRequest.of(pageParam.getPage(), pageParam.getSize());

        Slice<PostResponseDto> postList;

        if (StringUtils.isNullOrEmpty(pageParam.getCategory())) {
            postList = postRepository.findPostsNotDeleted(pageable).map(PostResponseDto::new);
        } else {
            postServiceHelper.validateCategory(pageParam.getCategory());
            postList = postRepository.findPostsByCategoryNotDeleted(pageParam.getCategory(), pageable).map(PostResponseDto::new);
        }

        for (PostResponseDto postResponseDto : postList) {
            Tuple tuple = postDetailsRepository.findBySelectImageAndContentById(postResponseDto.getPostId() * 10);
            postResponseDto.setPostImage(tuple.get(postDetails.image));
            postResponseDto.setContent(tuple.get(postDetails.content));
        }
        PageDto<List<PostResponseDto>> response = new PageDto<>(postList.hasNext(), postList.getContent());
        return response;
    }

    @Transactional(readOnly = true)
    public BaseResponseDto<?> getPostById(Long postId, UserDetailsImpl userDetails) {

        Post post = findById(postId);

        if (post.getDeletedAt() != null) {
            throw new IllegalArgumentException("삭제된 게시물 입니다.");
        }

        List<PostDetailsResponseDto> postDetailsResponseDtos = postDetailsRepository.findAllByPostId(postId).stream()
                .map(PostDetailsResponseDto::new)
                .toList();

        List<CommentResponseDto> commentResponseDtos = commentRepository.findAllByPostId(postId).stream()
                .map(CommentResponseDto::new)
                .toList();

        PostInfoResponseDto postInfoDto = new PostInfoResponseDto(post, postDetailsResponseDtos, commentResponseDtos);

        if (userDetails != null) {
            boolean like = isLike(postId, userDetails.getAccountInfo().getId());
            postInfoDto.setLike(like);
        }

        BaseResponseDto<?> response = new BaseResponseDto<>(HttpStatus.OK.toString(), "게시글 조회 성공", postInfoDto);
        return response;
    }

    // 게시글 생성
    @Transactional
    public BaseResponseDto<?> createPost(String category, List<MultipartFile> imageList,
        List<PostRequestDto> contentList, Integer imageCount, AccountInfo accountInfo) {

        postServiceHelper.validPostCreateRequest(imageList, contentList, imageCount, category);
        List<String> imageUrls = s3ImageUploader.storeImages(imageList,ImageFolderEnum.POST);
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

        Post savedPost = findById(postId);

        if (!postServiceHelper.hasRole(accountInfo,savedPost)){
            throw new PermissionDeniedException("권한이 없습니다.");
        }

        List<String> savedImages = postDetailsRepository.getPostImages(savedPost.getId());

        s3ImageUploader.deletePostImages(savedImages);

        savedPost.updatePost(category, imageCount);

        List<String> newImageUrls = s3ImageUploader.storeImages(imageList, ImageFolderEnum.POST);

        List<PostDetails> newPostDetails = PostDetails.createPostDetailsList(newImageUrls, contentList, savedPost, postServiceHelper.getMAX_IMAGE_COUNT());
        postDetailsRepository.saveAll(newPostDetails);

        BaseResponseDto<?> response = new BaseResponseDto<>(HttpStatus.OK.toString(), "게시글 수정 성공", null);
        return response;
    }

    @Transactional
    public BaseResponseDto deletePost(Long postId, AccountInfo accountInfo){
        boolean isPostOwner = postRepository.isPostOwner(postId, accountInfo);

        if (!postServiceHelper.hasRole(accountInfo, isPostOwner))
            throw new PermissionDeniedException("권한이 없습니다.");

        List<String> savedImages = postDetailsRepository.getPostImages(postId);
        postRepository.deletePost(postId, accountInfo);
        s3ImageUploader.deletePostImages(savedImages);

        return new BaseResponseDto(HttpStatus.OK.toString(), "게시글 삭제 성공",null);
    }

    public Post findById(Long postId) {
       return postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("해당하는 포스트가 없습니다."));
    }

    private boolean isLike(Long postId, Long AccountInfoId) {
        Like like = likeRepository.findByPostIdAndAccountId(postId, AccountInfoId);
        return like != null;
    }
}
