package com.clone.team4.global.handler;

import com.clone.team4.global.dto.BaseResponseDto;
import com.clone.team4.global.dto.ErrorResponseDto;
import com.clone.team4.global.exception.CustomStatusException;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static com.clone.team4.global.dto.BaseResponseDto.BaseMessageResponseDtoBuilder;

@Slf4j
@RestControllerAdvice
public class GlobalControllerExceptionHandler {

    @Value("${custom.option.debug}")
    public Boolean DEBUG_MODE;
    @ExceptionHandler(CustomStatusException.class)
    public ResponseEntity<BaseResponseDto> CustomStatusExceptionHandler(CustomStatusException exception){
        log.info("GlobalControllerExceptionHandler CustomStatusExceptionHandler");
        ErrorResponseDto errorResponseDto = new ErrorResponseDto(exception.getStatus().value(), exception.getMessage());
        BaseResponseDto responseDto = BaseResponseDto.MessageBuilder()
                .status(exception.getStatus().value())
                .msg(exception.getStatus().name())
                .addMessage(exception.getClass().getName(), exception.getMessage())
                .build();
        return ResponseEntity.status(exception.getStatus().value()).body(responseDto);
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<BaseResponseDto> MethodArgumentNotValidExceptionHandler(MethodArgumentNotValidException exception){
        log.info("GlobalControllerExceptionHandler MethodArgumentNotValidExceptionHandler");
        BaseMessageResponseDtoBuilder responseDto = BaseResponseDto.MessageBuilder().status(HttpStatus.SC_NOT_ACCEPTABLE).msg("not_acceptable");
        exception.getBindingResult().getFieldErrors().forEach(e -> responseDto.addMessage(e.getField(), e.getDefaultMessage()));
        return ResponseEntity.status(HttpStatusCode.valueOf(406)).body(responseDto.build());
    }
}
