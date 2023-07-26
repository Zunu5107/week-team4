package com.clone.team4.domain.user.controller;

import com.clone.team4.domain.user.dto.CheckEmailRequestDto;
import com.clone.team4.domain.user.dto.CheckNicknameRequestDto;
import com.clone.team4.domain.user.dto.SignupRequestDto;
import com.clone.team4.domain.user.service.UserService;
import com.clone.team4.global.dto.BaseResponseDto;
import com.clone.team4.global.dto.CustomStatusResponseDto;
import com.clone.team4.global.sercurity.UserDetailsImpl;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.sql.SQLIntegrityConstraintViolationException;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class UserController {

    private final UserService userService;


    @PostMapping("/signup")
    public ResponseEntity createAccount(@RequestBody @Valid SignupRequestDto requestDto) {
        return userService.createAccount(requestDto);
    }

    @PostMapping("/signup/email")
    public ResponseEntity checkEmail(@RequestBody @Valid CheckEmailRequestDto requestDto) {
        BaseResponseDto responseDto = userService.updateAccount(requestDto);
        return ResponseEntity.status(Integer.parseInt(responseDto.getStatus())).body(responseDto);
    }

    @PostMapping("/signup/nickname")
    public ResponseEntity checkNickname(@RequestBody @Valid CheckNicknameRequestDto requestDto) {
        BaseResponseDto responseDto = userService.updateAccount(requestDto);
        return ResponseEntity.status(Integer.parseInt(responseDto.getStatus())).body(responseDto);
    }

    @PutMapping("/update")
    public ResponseEntity updateAccount(@RequestPart(value = "image", required = false) MultipartFile image,
                                @RequestPart(value = "nickname", required = false) String nickname,
                                @RequestPart(value = "introduce", required = false) String introduce,
                                @AuthenticationPrincipal UserDetailsImpl userDetails) {
        System.out.println("userDetails.getAccountInfo().getId() = " + userDetails.getAccountInfo().getId());
        BaseResponseDto responseDto = userService.updateAccount(image, nickname, introduce, userDetails);
        return ResponseEntity.status(200).body(responseDto);
    }

    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public ResponseEntity<CustomStatusResponseDto> SQLIntegrityConstraintViolationExceptionHandler(HttpServletRequest request,
                                                                                                   HttpServletResponse response,
                                                                                                   SQLIntegrityConstraintViolationException exception) {
        return ResponseEntity.status(409).body(new CustomStatusResponseDto(false));
    }
}
