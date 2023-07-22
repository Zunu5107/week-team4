package com.clone.team4.global.filter;

import com.clone.team4.global.custom.CustomStaticMethodClass;
import com.clone.team4.global.dto.ErrorLoginMessageDto;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.io.IOException;

@Slf4j
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        log.info("가입되지 않은 사용자 접근");
        String temp = response.getHeader("AccessTokenDenide");
        if(temp != null){
            log.info("AccessTokenDenide");
            CustomStaticMethodClass.setFailResponse(response, new ErrorLoginMessageDto("RefreshToken Redirect", true, false));
        }
        else{
            log.info("Not AccessTokenDenide");
        }

        response.setStatus(401);
//      response.sendRedirect("/login");
    }
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public String HttpMessageNotReadableExceptionHandler(){
        return "핸들러 성공";
    }
}