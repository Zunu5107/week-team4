package com.clone.team4.domain.user.service;

import com.clone.team4.domain.user.dto.SignupRequestDto;
import com.clone.team4.domain.user.entity.AccountInfo;
import com.clone.team4.domain.user.entity.User;
import com.clone.team4.domain.user.repository.AccountInfoRepository;
import com.clone.team4.domain.user.repository.UserRepository;
import com.clone.team4.global.dto.CustomStatusResponseDto;
import com.clone.team4.global.exception.CustomStatusException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AccountInfoRepository accountInfoRepository;

    public ResponseEntity<CustomStatusResponseDto> createAccount(SignupRequestDto requestDto) {

        if(userRepository.findByEmail(requestDto.getEmail()).isPresent())
            throw CustomStatusException.builder("Same Email").status(HttpStatus.CONFLICT).build();
        if(accountInfoRepository.findByNickname(requestDto.getNickname()).isPresent())
            throw CustomStatusException.builder("Same nickname!").status(HttpStatus.CONFLICT).build();
        User user = new User(requestDto.getEmail(), passwordEncoder.encode(requestDto.getPassword()));
        AccountInfo accountInfo = new AccountInfo(user, requestDto.getNickname());

        userRepository.save(user);
        accountInfoRepository.save(accountInfo);

        return ResponseEntity.status(HttpStatus.CREATED.value()).body(new CustomStatusResponseDto(true));
    }
}
