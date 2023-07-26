package com.clone.team4.domain.user.dto;

import com.clone.team4.domain.user.dao.AccountContentDao;

import lombok.Getter;

@Getter
public class AccountInfoResponseDto {
    private String introduce;
    private String nickname;
    private String imageUrl;

    public AccountInfoResponseDto(AccountContentDao contentDao) {
        this.introduce = contentDao.getIntroduce();
        this.imageUrl = contentDao.getProfileImage();
        this.nickname = contentDao.getNickname();
    }
}
