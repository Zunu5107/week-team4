package com.clone.team4.domain.post.service;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.clone.team4.domain.post.dto.PostRequestDto;
import com.clone.team4.domain.post.entity.Post;
import com.clone.team4.domain.user.entity.AccountInfo;
import com.clone.team4.domain.user.entity.UserRoleEnum;

import lombok.Getter;

@Getter
@Component
public class PostServiceHelper {

    @Value("${max.image.count}")
    private int MAX_IMAGE_COUNT;

    private final String[] CATEGORY_WHITELIST = {"취미일상","집사진"};

    public void validPostCreateRequest(List<MultipartFile> images, List<PostRequestDto> contentList, Integer imageCount, String category) {
        if (!isValidImagesCount(images, contentList, imageCount) || !isValidCategoryRequest(category)) {
            throw new IllegalArgumentException("잘못된 입력입니다.");
        }
    }

    private boolean isValidImagesCount(List<MultipartFile> images, List<PostRequestDto> contentList, Integer imageCount) {
        int imageListSize = images.size();
        int contentListSize = contentList.size();
        return (imageListSize == imageCount && contentListSize == imageCount) &&
            (imageListSize > 0 && imageListSize <= MAX_IMAGE_COUNT) && (contentListSize > 0 && contentListSize <= MAX_IMAGE_COUNT);
    }

    private boolean isValidCategoryRequest(String category) {
        return Arrays.asList(CATEGORY_WHITELIST).contains(category);
    }

    public boolean hasRole(AccountInfo accountInfo, Post post) {
        return post.getAccountInfo().getId().equals(accountInfo.getId()) ||
            accountInfo.getRole().equals(UserRoleEnum.ADMIN);
    }
}
