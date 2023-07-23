package com.clone.team4.domain.user.controller;

import com.clone.team4.domain.user.dto.SignupRequestDto;
import com.clone.team4.domain.user.service.UserService;
import com.clone.team4.global.dto.CustomStatusResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLIntegrityConstraintViolationException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class UserController {

    private final UserService userService;


    @PostMapping("/signup")
    public ResponseEntity<CustomStatusResponseDto> createAccount(@RequestBody @Valid SignupRequestDto requestDto){
        return userService.createAccount(requestDto);
    }

    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public ResponseEntity<CustomStatusResponseDto> SQLIntegrityConstraintViolationExceptionHandler(HttpServletRequest request,
                                                                                                   HttpServletResponse response,
                                                                                                   SQLIntegrityConstraintViolationException exception){
        return ResponseEntity.status(409).body(new CustomStatusResponseDto(false));
    }
}
