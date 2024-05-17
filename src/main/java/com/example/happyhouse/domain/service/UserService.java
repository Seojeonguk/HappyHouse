package com.example.happyhouse.domain.service;

import com.example.happyhouse.domain.dto.request.LoginReq;
import com.example.happyhouse.domain.dto.request.UserRegistrationReq;

public interface UserService {
    String sigunUp(UserRegistrationReq userRegistrationReq);
    String login(LoginReq loginReq);
}