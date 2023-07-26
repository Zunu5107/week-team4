package com.clone.team4.domain.user.repository;

import com.clone.team4.domain.user.dao.AccountContentDao;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import static com.clone.team4.domain.user.entity.QAccountInfo.accountInfo;
import static com.clone.team4.domain.user.entity.QUser.user;

@Repository
@RequiredArgsConstructor
public class QAccountInfoRepositoryImpl implements QAccountInfoRepository{

    private final JPAQueryFactory queryFactory;
    @Override
    @Transactional
    public void updateAccountInfoContent(Long id, AccountContentDao contentDao) {
        queryFactory.update(accountInfo)
                .set(accountInfo.introduce, contentDao.getIntroduce())
                .set(accountInfo.profileImage, contentDao.getProfileImage())
                .set(accountInfo.nickname, contentDao.getNickname())
                .where(accountInfo.id.eq(id))
                .execute();
    }

    @Override
    public boolean findByEmailIsPresent(String email) {
        return queryFactory.select(user.id).from(user).where(user.email.eq((email))).fetchFirst() != null;
    }

    @Override
    public boolean findByNickNameIsPresent(String nickname) {
        return queryFactory.select(accountInfo.id).from(accountInfo).where(accountInfo.nickname.eq((nickname))).fetchFirst() != null;
    }


}
