package com.clone.team4.global.custom;

import com.clone.team4.global.dto.ErrorLoginMessageDto;
import com.clone.team4.global.redis.AuthenticationRedisService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;

import static com.clone.team4.global.redis.AuthenticationRedisService.AuthenticationStringEnum.ACCESS_TOKEN;
import static com.clone.team4.global.redis.AuthenticationRedisService.AuthenticationStringEnum.REFRESH_TOKEN;

@Slf4j
public class CustomStaticMethodClass {

    @Value("${custom.option.originLink}")
    public static String Origin;

    public static void setFailResponse(HttpServletResponse response, ErrorLoginMessageDto errorLoginDto) throws IOException {
//        Collection <String> em = response.getHeaderNames();
//        for (String s : em) {
//            String value = response.getHeader(s);
//            log.info("Header name = " + s);
//            log.info("Header value = " + value);
//        }
        log.info("set Fail Response");
        if (!response.isCommitted()) {
            response.resetBuffer();
        }
        response.setHeader("Access-Control-Allow-Origin", Origin);
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Methods","*");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers",
                "Origin, X-Requested-With, Content-Type, Accept, Authorization");
        response.setStatus(401);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        ObjectMapper objectMapper = new ObjectMapper();
        String str = objectMapper.writeValueAsString(errorLoginDto);
        if(!response.getOutputStream().isReady()){
            response.getOutputStream().write(str.getBytes());
        }
    }

    public static void setFailResponseToken(HttpServletResponse response, AuthenticationRedisService authenticationRedisService) throws IOException {
//        Collection <String> em = response.getHeaderNames();
//        for (String s : em) {
//            String value = response.getHeader(s);
//            log.info("Header name = " + s);
//            log.info("Header value = " + value);
//        }
        log.info("set Fail ResponseToken");
        String access = authenticationRedisService.getValues(ACCESS_TOKEN.getAuthenticationString());
        String refresh = authenticationRedisService.getValues(REFRESH_TOKEN.getAuthenticationString());
        if(access == null && refresh == null){
            return;
        }
        response.setHeader("Access-Control-Allow-Origin", Origin);
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Methods","*");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers",
                "Origin, X-Requested-With, Content-Type, Accept, Authorization");
        response.setStatus(401);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        ErrorLoginMessageDto errorLoginDto = new ErrorLoginMessageDto();
        if(access != null){
            errorLoginDto.setAccessValidationError(true);
            errorLoginDto.setMessage(access);
            authenticationRedisService.deleteKey(ACCESS_TOKEN.getAuthenticationString());
        }
        if(refresh != null){
            errorLoginDto.setRefreshValidationError(true);
            errorLoginDto.setMessage(refresh);
            authenticationRedisService.deleteKey(REFRESH_TOKEN.getAuthenticationString());
        }
        ObjectMapper objectMapper = new ObjectMapper();
        String str = objectMapper.writeValueAsString(errorLoginDto);
        if(!response.getOutputStream().isReady()){
            response.getOutputStream().write(str.getBytes());
        }
    }
    public static void checkResponseBody(HttpServletResponse response, ErrorLoginMessageDto errorLoginDto) throws IOException {
//        Collection <String> em = response.getHeaderNames();
//        for (String s : em) {
//            String value = response.getHeader(s);
//            log.info("Header name = " + s);
//            log.info("Header value = " + value);
//        }
    }
}
