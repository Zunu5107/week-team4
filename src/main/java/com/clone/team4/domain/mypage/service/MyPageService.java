package com.clone.team4.domain.mypage.service;

import com.clone.team4.domain.mypage.dto.MyPagePostResponseDto;
import com.clone.team4.domain.mypage.dto.MyPageResponseDto;
import com.clone.team4.domain.mypage.repository.MypageRepository;
import com.clone.team4.domain.user.entity.AccountInfo;
import com.clone.team4.domain.user.repository.AccountInfoRepository;
import com.clone.team4.global.dto.BaseResponseDto;
import com.clone.team4.global.exception.CustomStatusException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MyPageService {

    private final MypageRepository mypageRepository;
    private final AccountInfoRepository accountInfoRepository;
    public BaseResponseDto<?> getMyPage(AccountInfo accountInfo) {
        MyPageResponseDto result = new MyPageResponseDto();
        result.setIntroduce(accountInfo.getIntroduce());
        result.setNickname(accountInfo.getNickname());
        result.setUserImage(accountInfo.getProfileImage());
        List<MyPagePostResponseDto> MyPost = mypageRepository.findByMyPost(accountInfo.getId());
        if(MyPost.size() > 0)
            result.setPostList(MyPost);
        List<MyPagePostResponseDto> Likelist = mypageRepository.findByMyLikePost(accountInfo.getId());
        if(Likelist.size() > 0)
            result.setLikeList(Likelist);
        return BaseResponseDto.builder()
                .msg("success")
                .status(200)
                .data(result)
                .build();
    }

    public BaseResponseDto getMyPageForNickName(String nickname) {
        AccountInfo selectId = accountInfoRepository.findByNickname(nickname).orElseThrow(() -> CustomStatusException.builder("찾는 아이디가 없습니다.").build());
        MyPageResponseDto result = new MyPageResponseDto();
        result.setIntroduce(selectId.getIntroduce());
        result.setNickname(selectId.getNickname());
        result.setUserImage(selectId.getProfileImage());
        result.setPostList(mypageRepository.findByMyPost(selectId.getId()));
        return BaseResponseDto.builder()
                .msg("success")
                .status(200)
                .data(result)
                .build();
    }
}
