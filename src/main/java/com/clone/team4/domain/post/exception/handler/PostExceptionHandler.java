package com.clone.team4.domain.post.exception.handler;

import com.clone.team4.domain.post.exception.PostNotFoundException;
import com.clone.team4.global.dto.BaseResponseDto;
import com.clone.team4.global.exception.PermissionDeniedException;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static org.slf4j.LoggerFactory.getLogger;

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
    
    private static Logger logger() {
        final class LogHolder {
            private static final Logger LOGGER = getLogger(PostExceptionHandler.class);
        }
        return LogHolder.LOGGER;
    }
}
