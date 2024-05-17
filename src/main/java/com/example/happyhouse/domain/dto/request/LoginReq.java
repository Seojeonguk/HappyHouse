package com.example.happyhouse.domain.dto.request;

import com.example.happyhouse.domain.entity.User;
import lombok.Getter;

@Getter
public class LoginReq {
    String loginId;
    String pw;

    public User toEntity() {
        return User.builder().loginId(loginId).pw(pw).build();
    }
}
