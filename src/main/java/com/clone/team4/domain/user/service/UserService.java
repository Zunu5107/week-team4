package com.clone.team4.domain.user.service;

import com.clone.team4.domain.user.dao.AccountContentDao;
import com.clone.team4.domain.user.dto.AccountInfoResponseDto;
import com.clone.team4.domain.user.dto.SignupRequestDto;
import com.clone.team4.domain.user.entity.AccountInfo;
import com.clone.team4.domain.user.entity.User;
import com.clone.team4.domain.user.repository.AccountInfoRepository;
import com.clone.team4.domain.user.repository.UserRepository;
import com.clone.team4.global.dto.BaseResponseDto;
import com.clone.team4.global.dto.CustomMessageResponseDto;
import com.clone.team4.global.dto.CustomStatusResponseDto;
import com.clone.team4.global.exception.CustomStatusException;
import com.clone.team4.global.image.ImageFolderEnum;
import com.clone.team4.global.image.S3ImageUploader;
import com.clone.team4.global.sercurity.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AccountInfoRepository accountInfoRepository;
    private final S3ImageUploader imageUploader;

    public ResponseEntity createAccount(SignupRequestDto requestDto) {
        log.info("in create account");
        if(userRepository.findByEmail(requestDto.getEmail()).isPresent())
            return ResponseEntity.status(409).body(
                    new BaseResponseDto<>("409", "CONFLICT",
                            CustomMessageResponseDto.builder("email", "same email error").build()));
        log.info("search email");
        if(accountInfoRepository.findByNickname(requestDto.getNickname()).isPresent())
            return ResponseEntity.status(409).body(
                    new BaseResponseDto<>("409", "CONFLICT",
                            CustomMessageResponseDto.builder("nickname", "same nickname error").build()));
        log.info("search nickname");

        if (userRepository.findByEmail(requestDto.getEmail()).isPresent())
            throw CustomStatusException.builder("Same Email").status(HttpStatus.CONFLICT).build();
        if (accountInfoRepository.findByNickname(requestDto.getNickname()).isPresent())
            throw CustomStatusException.builder("Same nickname!").status(HttpStatus.CONFLICT).build();
        User user = new User(requestDto.getEmail(), passwordEncoder.encode(requestDto.getPassword()));
        AccountInfo accountInfo = new AccountInfo(user, requestDto.getNickname());

        userRepository.save(user);
        accountInfoRepository.save(accountInfo);

        return ResponseEntity.status(HttpStatus.CREATED.value()).body(new CustomStatusResponseDto(true));
    }

    public BaseResponseDto updateAccount(MultipartFile image, String nickname, String introduce, UserDetailsImpl userDetails) {
        String profileImage = imageUploader.storeImage(image, ImageFolderEnum.POST);
        AccountContentDao setAccount = new AccountContentDao(introduce, profileImage, nickname);
        accountInfoRepository.updateAccountInfoContent(userDetails.getUser().getId(), setAccount);

        return BaseResponseDto.builder().status(200).msg("success").data(new AccountInfoResponseDto(setAccount)).build();
    }
}

/*{
  “status” : 200
 “msg” : success
  “data”: {
     “userImage” : “userImage”
   }
 }
 */
