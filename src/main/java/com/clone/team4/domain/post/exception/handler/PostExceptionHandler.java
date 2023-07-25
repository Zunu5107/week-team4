package com.clone.team4.domain.post.exception.handler;

import com.clone.team4.domain.post.exception.PostNotFoundException;
import com.clone.team4.global.dto.BaseResponseDto;
import com.clone.team4.global.exception.PermissionDeniedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public interface PostExceptionHandler {

    @ExceptionHandler({PostNotFoundException.class, IllegalArgumentException.class})
    default ResponseEntity<BaseResponseDto> postExceptionHandler(RuntimeException e) {
        BaseResponseDto response = BaseResponseDto.builder()
            .status(HttpStatus.BAD_REQUEST.value())
            .msg(e.getMessage())
            .build();
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(PermissionDeniedException.class)
    default ResponseEntity<BaseResponseDto> postPermissionDeniedExceptionHandler(RuntimeException e) {
        BaseResponseDto response = BaseResponseDto.builder()
            .status(HttpStatus.FORBIDDEN.value())
            .msg(e.getMessage())
            .build();
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
    }

    @ExceptionHandler(RuntimeException.class)
    default ResponseEntity<BaseResponseDto> postRuntimeExceptionHandler(RuntimeException e) {
        BaseResponseDto response = BaseResponseDto.builder()
            .status(HttpStatus.BAD_REQUEST.value())
            .msg(e.getMessage())
            .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
}
