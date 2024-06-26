package com.example.happyhouse.domain.dto.request;

import com.example.happyhouse.domain.entity.RoleType;
import com.example.happyhouse.domain.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserRegistrationReq {
    String loginId;
    String pw;
    String name;
    String addr;
    String tel;
    RoleType role;

    public User toEntity() {
        return User.builder()
                .loginId(loginId)
                .pw(pw)
                .name(name)
                .addr(addr)
                .tel(tel)
                .role(role)
                .build();
    }
}
