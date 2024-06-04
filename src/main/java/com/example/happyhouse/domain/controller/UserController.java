package com.example.happyhouse.domain.controller;

import com.example.happyhouse.domain.dto.request.LoginReq;
import com.example.happyhouse.domain.dto.request.UserRegistrationReq;
import com.example.happyhouse.domain.dto.response.TokenRes;
import com.example.happyhouse.domain.dto.response.UserInfoRes;
import com.example.happyhouse.domain.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
@Slf4j
public class UserController {

    private final UserService userService;

    @PostMapping("/signUp")
    public ResponseEntity<String> signUp(@RequestBody UserRegistrationReq userRegistrationReq) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.sigunUp(userRegistrationReq));
    }

    @PostMapping("/login")
    public ResponseEntity<TokenRes> login(@RequestBody LoginReq loginReq) {
        return ResponseEntity.ok().body(userService.login(loginReq));
    }

    @PostMapping("/me")
    public ResponseEntity<UserInfoRes> getMyInfo(Authentication authentication) {
        return ResponseEntity.ok().body(userService.getMyInfo(authentication));
    }
}
