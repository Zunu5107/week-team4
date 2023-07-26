package com.clone.team4.global.jwt;

import com.clone.team4.domain.user.entity.AccountInfo;
import com.clone.team4.global.dto.ErrorLoginMessageDto;
import com.clone.team4.global.redis.AuthenticationRedisService;
import com.clone.team4.global.sercurity.UserDetailsImpl;
import com.clone.team4.global.sercurity.UserDetailsServiceImpl;
import io.jsonwebtoken.*;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Enumeration;

import static com.clone.team4.global.custom.CustomStaticMethodClass.setFailResponse;
import static com.clone.team4.global.jwt.JwtUtil.ACCESS_HEADER;
import static com.clone.team4.global.redis.AuthenticationRedisService.AuthenticationStringEnum.ACCESS_TOKEN;
import static com.clone.team4.global.redis.AuthenticationRedisService.AuthenticationStringEnum.REFRESH_TOKEN;

@Slf4j(topic = "JWT 검증 및 인가")
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    @Value("${custom.option.debug}")
    private Boolean Debug;
    private final JwtUtil jwtUtil;
    private final UserDetailsServiceImpl userDetailsService;

    private final AuthenticationRedisService authenticationRedisService;

    public JwtAuthorizationFilter(JwtUtil jwtUtil, UserDetailsServiceImpl userDetailsService, AuthenticationRedisService authenticationRedisService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
        this.authenticationRedisService = authenticationRedisService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain filterChain) throws ServletException, IOException {
        if(req.getMethod().equals(HttpMethod.OPTIONS.name()))
            return;
//                 헤더 확인하기
        Enumeration em = req.getHeaderNames();
        while(em.hasMoreElements()) {
            String name = (String)em.nextElement();
            String value = req.getHeader(name);
            System.out.println("name = " + name);
            System.out.println("value = " + value);
        }
//
//        for (Part part : req.getParts()) {
//            InputStream inputStream = part.getInputStream();
//            System.out.println("part.getName() = " + part.getName());
//            byte[] rawData = StreamUtils.copyToByteArray(inputStream);
//            String result = new String(rawData);
//            System.out.println("part.result = " + result);
//        }
//        System.out.println("req.getInputStream() = " + req.getInputStream());


        //String tokenValue = jwtUtil.getTokenFromRequest(req);
        log.info("Token Filter");
        String tokenValue = jwtUtil.getTokenFromRequestHeader(JwtUtil.ACCESS_HEADER, req);
        String tokenValueRefresh = jwtUtil.getTokenFromRequestHeader(JwtUtil.AUTHORIZATION_HEADER, req);

        if (StringUtils.hasText(tokenValue)) {
            log.info("AccessToken");
            // JWT 토큰 substring
            try {
                tokenValue = jwtUtil.substringToken(tokenValue);
                log.info(tokenValue);

                if (!jwtUtil.validateToken(tokenValue)) {
                    log.error("AccessToken Error");
                    return;
                }

                log.info("Vaildate Correct");
                Claims info = jwtUtil.getUserInfoFromToken(tokenValue);

                setAuthentication(info.getSubject());

            } catch (Exception e) {
                exceptionHandler(res, e);
            }
        }
        else if (StringUtils.hasText(tokenValueRefresh)) {
            log.info("RefreshToken");
            try {
                GetRefreshToken(req, res, tokenValueRefresh);
            } catch (Exception e) {
                exceptionHandlerRefresh(res, e);
            }
        }
        else{
            log.info("AccessTokenDenide");
        }
        filterChain.doFilter(req, res);
        authenticationRedisService.deleteKey(ACCESS_TOKEN.getAuthenticationString());
        authenticationRedisService.deleteKey(REFRESH_TOKEN.getAuthenticationString());
    }

    private void GetRefreshToken(HttpServletRequest req, HttpServletResponse res, String tokenValue) throws IOException{
        if (StringUtils.hasText(tokenValue)) {
            // JWT 토큰 substring
            try {
                tokenValue = jwtUtil.substringToken(tokenValue);
                log.info(tokenValue);

                if (!jwtUtil.validateToken(tokenValue)) {
                    log.error("RefreshToken Error");
                    return;
                }
            } catch (Exception e) {
                exceptionHandlerRefresh(res, e);
                return;
            }

            log.info("Vaildate Correct");
            Claims info = jwtUtil.getUserInfoFromToken(tokenValue);
            String subject = info.getSubject();
            String uuid = userDetailsService.decryptAES(subject);
            String username = userDetailsService.loadUsernameByRedis(uuid);
            if(username == null)
                exceptionHandler(res, new NullPointerException());
            setAuthenticationRefresh(res, username);
        }
    }

    // 인증 처리
    public void setAuthentication(String username) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication authentication = createAuthentication(username);
        context.setAuthentication(authentication);

        SecurityContextHolder.setContext(context);
    }

    public void setAuthenticationRefresh(HttpServletResponse response, String username) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication authentication = createAuthenticationRefresh(response, username);
        context.setAuthentication(authentication);

        SecurityContextHolder.setContext(context);
    }

    // 인증 객체 생성
    private Authentication createAuthentication(String nickname) {
        UserDetails userDetails = userDetailsService.loadUserByAccountInfo(nickname);
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

    private Authentication createAuthenticationRefresh(HttpServletResponse response, String nickname) {
        UserDetails userDetails = userDetailsService.loadUserByAccountInfo(nickname);
        AccountInfo accountInfo = ((UserDetailsImpl) userDetails).getAccountInfo();
        String accessToken = jwtUtil.createToken(accountInfo.getNickname(),accountInfo.getRole());
        jwtUtil.addJwtToHeader(accessToken, ACCESS_HEADER, response);
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

    public void exceptionHandler(HttpServletResponse response, Exception exception) throws IOException {
        log.info("Exception Handler");
        ErrorLoginMessageDto messageDto = new ErrorLoginMessageDto();
        if (exception instanceof SecurityException || exception instanceof MalformedJwtException || exception instanceof SignatureException) {
            messageDto.setMessage("Invalid JWT signature, 유효하지 않는 JWT 서명 입니다.");
        } else if (exception instanceof ExpiredJwtException) {
            messageDto.setMessage("Expired JWT token, 만료된 JWT token 입니다.");
        } else if (exception instanceof UnsupportedJwtException) {
            messageDto.setMessage("Unsupported JWT token, 지원되지 않는 JWT 토큰 입니다.");
        } else if (exception instanceof IllegalArgumentException) {
            messageDto.setMessage("JWT claims is empty, 잘못된 JWT 토큰 입니다.");
        } else if (exception instanceof NullPointerException) {
            messageDto.setMessage("Not Found JWT.");
        } else if (Debug)
            messageDto.setMessage(exception.getClass().getCanonicalName()); // Debug
//        setFailResponse(response, messageDto);
        authenticationRedisService.setValues(ACCESS_TOKEN.getAuthenticationString(), messageDto.getMessage());
    }

    private void exceptionHandlerRefresh(HttpServletResponse response, Exception exception) throws IOException {
        log.info("Exception Handler");
        ErrorLoginMessageDto messageDto = new ErrorLoginMessageDto();
        if (exception instanceof SecurityException || exception instanceof MalformedJwtException || exception instanceof SignatureException) {
            messageDto.setMessage("Invalid JWT signature, 유효하지 않는 JWT 서명 입니다.");
        } else if (exception instanceof ExpiredJwtException) {
            messageDto.setMessage("Expired JWT token, 만료된 JWT token 입니다.");
        } else if (exception instanceof UnsupportedJwtException) {
            messageDto.setMessage("Unsupported JWT token, 지원되지 않는 JWT 토큰 입니다.");
        } else if (exception instanceof IllegalArgumentException) {
            messageDto.setMessage("JWT claims is empty, 잘못된 JWT 토큰 입니다.");
        } else if (exception instanceof NullPointerException) {
            messageDto.setMessage("Not Found JWT.");
        }else if (Debug)
            messageDto.setMessage(exception.getClass().getCanonicalName()); // Debug
        messageDto.setRefreshValidationError(true);
        authenticationRedisService.setValues(REFRESH_TOKEN.getAuthenticationString(), messageDto.getMessage());
    }

    private void exceptionHandlerAccess(HttpServletResponse response, Exception exception) throws IOException {
        log.info("Exception Handler Access");
        ErrorLoginMessageDto messageDto = new ErrorLoginMessageDto();
        if (exception instanceof SecurityException || exception instanceof MalformedJwtException || exception instanceof SignatureException) {
            messageDto.setMessage("Invalid JWT signature, 유효하지 않는 JWT 서명 입니다.");
        } else if (exception instanceof ExpiredJwtException) {
            messageDto.setMessage("Expired JWT token, 만료된 JWT token 입니다.");
        } else if (exception instanceof UnsupportedJwtException) {
            messageDto.setMessage("Unsupported JWT token, 지원되지 않는 JWT 토큰 입니다.");
        } else if (exception instanceof IllegalArgumentException) {
            messageDto.setMessage("JWT claims is empty, 잘못된 JWT 토큰 입니다.");
        } else if (Debug)
            messageDto.setMessage(exception.getClass().getCanonicalName()); // Debug
        messageDto.setAccessValidationError(true);
        authenticationRedisService.setValues(ACCESS_TOKEN.getAuthenticationString(), messageDto.getMessage());
    }


}
