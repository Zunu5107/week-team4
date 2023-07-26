package com.clone.team4.domain.user.repository;

import com.clone.team4.domain.user.dao.AccountContentDao;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.jpa.impl.JPAUpdateClause;
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
        JPAUpdateClause jpaUpdateClause =  queryFactory.update(accountInfo);
        if(contentDao.getIntroduce() != null)
            jpaUpdateClause.set(accountInfo.introduce, contentDao.getIntroduce());
        if(contentDao.getProfileImage() != null)
            jpaUpdateClause.set(accountInfo.profileImage, contentDao.getProfileImage());
        if(contentDao.getNickname() != null)
            jpaUpdateClause.set(accountInfo.nickname, contentDao.getNickname());
        jpaUpdateClause.where(accountInfo.id.eq(id)).execute();
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
