package com.clone.team4.global.filter;

import com.clone.team4.global.custom.CustomStaticMethodClass;
import com.clone.team4.global.dto.ErrorLoginMessageDto;
import com.clone.team4.global.redis.AuthenticationRedisService;
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

    private final AuthenticationRedisService authenticationRedisService;
    public CustomAuthenticationEntryPoint(AuthenticationRedisService authenticationRedisService){
        this.authenticationRedisService = authenticationRedisService;
    }
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        log.info("가입되지 않은 사용자 접근");
        System.out.println("authException.getClass().getName() = " + authException.getClass().getName());
        CustomStaticMethodClass.setFailResponseToken(response, authenticationRedisService);
//        response.setStatus(401);
//      response.sendRedirect("/login");
    }
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public String HttpMessageNotReadableExceptionHandler(){
        return "핸들러 성공";
    }
}
