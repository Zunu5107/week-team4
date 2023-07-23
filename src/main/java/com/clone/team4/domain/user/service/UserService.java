package com.clone.team4.domain.user.service;

import com.clone.team4.domain.user.dto.SignupRequestDto;
import com.clone.team4.domain.user.entity.AccountInfo;
import com.clone.team4.domain.user.entity.User;
import com.clone.team4.domain.user.repository.AccountInfoRepository;
import com.clone.team4.domain.user.repository.UserRepository;
import com.clone.team4.global.dto.BaseResponseDto;
import com.clone.team4.global.dto.CustomMessageResponseDto;
import com.clone.team4.global.dto.CustomStatusResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AccountInfoRepository accountInfoRepository;
    private final RestTemplateBuilder builder;
//    private final RestTemplate restTemplate = builder.build();

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

        User user = new User(requestDto.getEmail(), passwordEncoder.encode(requestDto.getPassword()));
        AccountInfo accountInfo = new AccountInfo(user, requestDto.getNickname());

        userRepository.save(user);
        accountInfoRepository.save(accountInfo);

        return ResponseEntity.status(HttpStatus.CREATED.value()).body(new CustomStatusResponseDto(true));
    }

    public ResponseEntity loginkakao() {
        URI uri = UriComponentsBuilder
                .fromUriString("https://kauth.kakao.com")
                .path("/oauth/token")
                .encode()
                .build()
                .toUri();
        log.info("uri = " + uri);
        return ResponseEntity.ok().build();
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
