package com.clone.team4.domain.mypage.repository;

import com.clone.team4.domain.mypage.dto.MyPagePostResponseDto;
import com.clone.team4.global.exception.CustomStatusException;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

import static com.clone.team4.domain.like.entity.QLike.like;
import static com.clone.team4.domain.post.entity.QPost.post;
import static com.clone.team4.domain.post.entity.QPostDetails.postDetails;

@Slf4j
@Repository
@RequiredArgsConstructor
public class MypageRepository {
    private final JPAQueryFactory queryFactory;

    public List<MyPagePostResponseDto> findByMyPost(Long userId){
        List<MyPagePostResponseDto> result = new ArrayList<>();
        List<Long> postList = queryFactory.select(post.id).from(post).where(post.accountInfo.id.eq(userId),post.deletedAt.isNull()).fetch();
        for (Long aLong : postList) {
            log.info(aLong.toString());
            result.add(new MyPagePostResponseDto(aLong, findByPostImageById(aLong)));
        }
        return result;
    }

    public List<MyPagePostResponseDto> findByMyLikePost(Long userId){
        List<MyPagePostResponseDto> result = new ArrayList<>();
        List<Long> postList = queryFactory.select(like.post.id).from(like).where(like.accountInfo.id.eq(userId),like.post.deletedAt.isNull()).fetch();
        for (Long aLong : postList) {
            result.add(new MyPagePostResponseDto(aLong, findByPostImageById(aLong)));
        }
        return result;
    }


    public String findByPostImageById(Long id){
        String first = queryFactory.select(postDetails.image).from(postDetails).where(postDetails.post.id.eq(id)).fetchFirst();
        if(first == null)
            throw CustomStatusException.builder("포스트 이미지가 없습니다.").status(404).build();
        return first;
    }


}
