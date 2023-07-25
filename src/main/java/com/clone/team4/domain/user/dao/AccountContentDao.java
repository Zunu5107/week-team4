package com.clone.team4.domain.user.dao;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
public class AccountContentDao {
    String introduce;
    String profileImage;
    String nickname;

    public AccountContentDao(String introduce, String profileImage, String nickname) {
        if(introduce != null)
            this.introduce = introduce;
        if(profileImage != null)
            this.profileImage = profileImage;
        if(nickname != null)
            this.nickname = nickname;
    }
}
