package com.clone.team4.domain.mypage.controller;

import com.clone.team4.global.sercurity.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class MyPageController {
    @GetMapping()
    public String test(@AuthenticationPrincipal UserDetailsImpl userDetails){
        return userDetails.getAccountInfo().getNickname();
    }
}
