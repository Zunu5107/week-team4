package com.clone.team4.domain.post.service;

import com.clone.team4.domain.post.dto.PostRequestDto;
import com.clone.team4.domain.post.entity.Post;
import com.clone.team4.domain.user.entity.AccountInfo;
import com.clone.team4.domain.user.entity.UserRoleEnum;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;

@Getter
@Component
public class PostServiceHelper {

    @Value("${max.image.count}")
    private int MAX_IMAGE_COUNT;

    private final String[] CATEGORY_WHITELIST = {"취미일상","집사진"};

    public void validPostCreateRequest(List<MultipartFile> images, List<PostRequestDto> contentList, Integer imageCount, String category) {
        validateImageAndContentCount(images, contentList, imageCount);
        validateCategory(category);
        validationContentNotBlank(contentList);
    }

    private void validateImageAndContentCount(List<MultipartFile> images, List<PostRequestDto> contentList, Integer imageCount) {
        int imageListSize = images.size();
        int contentListSize = contentList.size();
        if ((imageListSize != imageCount || contentListSize != imageCount) ||
            (imageListSize == 0 || imageListSize > MAX_IMAGE_COUNT) || (contentListSize == 0 || contentListSize > MAX_IMAGE_COUNT)){
            throw new IllegalArgumentException("잘못된 이미지 입력입니다.");
        }
    }

    private void validationContentNotBlank(List<PostRequestDto> contentList) {
        for (PostRequestDto postRequestDto : contentList) {
            if (!StringUtils.hasText(postRequestDto.getContent())){
                throw new IllegalArgumentException("내용은 공백일 수 없습니다.");
            }
        }
    }

    public void validateCategory(String category) {
        if(!Arrays.asList(CATEGORY_WHITELIST).contains(category)) {
            throw new IllegalArgumentException("잘못된 카테고리 입력입니다.");
        }
    }

    public boolean hasRole(AccountInfo accountInfo, Post post) {
        return post.getAccountInfo().getId().equals(accountInfo.getId()) ||
            accountInfo.getRole().equals(UserRoleEnum.ADMIN);
    }

    public boolean hasRole(AccountInfo accountInfo, boolean isPostOwner) {
        return isPostOwner || accountInfo.getRole().equals(UserRoleEnum.ADMIN);
    }
}
