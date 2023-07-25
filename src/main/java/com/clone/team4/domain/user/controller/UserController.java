package com.clone.team4.domain.user.controller;

import com.clone.team4.domain.user.dto.SignupRequestDto;
import com.clone.team4.domain.user.service.UserService;
import com.clone.team4.global.dto.CustomStatusResponseDto;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class UserController {

    private final UserService userService;


    @PostMapping("/signup")
    public ResponseEntity createAccount(@RequestBody @Valid SignupRequestDto requestDto){
        return userService.createAccount(requestDto);
    }

//    @PostMapping("/kakao/login")
//    public ResponseEntity loginKakao(){
//        return userService.loginkakao();
//    }

    @GetMapping("/oauth")
    public ResponseEntity oauthKakao(@RequestParam(required = false) String code,
                                     @RequestParam(required = false) String error,
                                     @RequestParam(required = false) String error_description,
                                     @RequestParam(required = false) String state){
//        return userService.loginkakao();
        Map<String, String> result = new LinkedHashMap<>();
        result.put("code", code);
        log.info(code);
        result.put("error", error);
        log.info(error);
        result.put("error_description", error_description);
        log.info(error_description);
        result.put("state", state);
        log.info(state);
        return ResponseEntity.ok(result);
    }

    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public ResponseEntity<CustomStatusResponseDto> SQLIntegrityConstraintViolationExceptionHandler(HttpServletRequest request,
                                                                                                   HttpServletResponse response,
                                                                                                   SQLIntegrityConstraintViolationException exception){
        return ResponseEntity.status(409).body(new CustomStatusResponseDto(false));
    }
}
