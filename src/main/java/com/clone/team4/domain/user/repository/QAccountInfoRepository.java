package com.clone.team4.domain.user.repository;

import com.clone.team4.domain.user.dao.AccountContentDao;

public interface QAccountInfoRepository {

    void updateAccountInfoContent(Long id, AccountContentDao contentDao);

    boolean findByEmailIsPresent(String email);
    boolean findByNickNameIsPresent(String nickname);

}
