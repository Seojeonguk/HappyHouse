package com.example.happyhouse.domain.dto.response;

import com.example.happyhouse.domain.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserInfoRes {
    String loginId;
    String name;
    String addr;
    String tel;

    public UserInfoRes(User user) {
        this.loginId = user.getLoginId();
        this.name = user.getName();
        this.addr = user.getAddr();
        this.tel = user.getTel();
    }
}
