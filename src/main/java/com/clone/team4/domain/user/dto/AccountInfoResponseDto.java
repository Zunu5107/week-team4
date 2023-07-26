package com.clone.team4.domain.user.dto;

import com.clone.team4.domain.user.dao.AccountContentDao;
import com.clone.team4.domain.user.entity.AccountInfo;
import lombok.Getter;

@Getter
public class AccountInfoResponseDto {
    private String introduce;
    private String nickname;
    private String imageUrl;

    public AccountInfoResponseDto(AccountInfo accountInfo, AccountContentDao contentDao) {
        this.introduce = contentDao.getIntroduce() != null ? contentDao.getIntroduce() : accountInfo.getIntroduce();
        this.imageUrl = contentDao.getProfileImage() != null ? contentDao.getProfileImage() : accountInfo.getProfileImage();;
        this.nickname = contentDao.getNickname() != null ? contentDao.getNickname() : accountInfo.getNickname();;
    }
}
