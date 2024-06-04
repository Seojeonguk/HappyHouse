package com.example.happyhouse.domain.controller;

import com.example.happyhouse.domain.dto.request.LoginReq;
import com.example.happyhouse.domain.dto.request.RefreshReq;
import com.example.happyhouse.domain.dto.request.UserRegistrationReq;
import com.example.happyhouse.domain.dto.response.TokenRes;
import com.example.happyhouse.domain.dto.response.UserInfoRes;
import com.example.happyhouse.domain.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

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

    @DeleteMapping()
    public ResponseEntity<Void> deleteUser(Authentication authentication) {
        userService.deleteUser(authentication);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PostMapping("/refresh")
    public ResponseEntity<TokenRes> refreshToken(@RequestBody RefreshReq refreshReq) {
        return ResponseEntity.ok().body(userService.refresh(refreshReq));
    }

    @PutMapping()
    public ResponseEntity<Void> modifyUser(Authentication authentication, @RequestBody UserRegistrationReq userRegistrationReq) {
        userService.modifyUser(authentication, userRegistrationReq);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
