package com.example.happyhouse.domain.service;

import com.example.happyhouse.domain.dto.request.LoginReq;
import com.example.happyhouse.domain.dto.request.UserRegistrationReq;
import com.example.happyhouse.domain.dto.response.TokenRes;
import com.example.happyhouse.domain.dto.response.UserInfoRes;
import org.springframework.security.core.Authentication;

public interface UserService {
    String sigunUp(UserRegistrationReq userRegistrationReq);
    TokenRes login(LoginReq loginReq);
    UserInfoRes getMyInfo(Authentication authentication);
}
