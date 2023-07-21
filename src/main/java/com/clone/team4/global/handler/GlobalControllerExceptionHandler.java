package com.clone.team4.global.handler;

import com.clone.team4.global.dto.CustomStatusAndMessageResponseDto;
import com.clone.team4.global.dto.ErrorResponseDto;
import com.clone.team4.global.exception.CustomStatusException;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalControllerExceptionHandler {
    @ExceptionHandler(CustomStatusException.class)
    public ResponseEntity<ErrorResponseDto> CustomStatusExceptionHandler(CustomStatusException exception){
        ErrorResponseDto errorResponseDto = new ErrorResponseDto(exception.getStatus().value(), exception.getMessage());
        return ResponseEntity.status(exception.getStatus().value()).body(errorResponseDto);
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<CustomStatusAndMessageResponseDto> MethodArgumentNotValidExceptionHandler(MethodArgumentNotValidException exception){
        CustomStatusAndMessageResponseDto responseDto = new CustomStatusAndMessageResponseDto(false);
        exception.getBindingResult().getFieldErrors().forEach(e -> responseDto.addMessage(e.getField(), e.getDefaultMessage()));
        return ResponseEntity.status(HttpStatusCode.valueOf(406)).body(responseDto);
    }
}
