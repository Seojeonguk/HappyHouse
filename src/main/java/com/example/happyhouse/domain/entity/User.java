package com.example.happyhouse.domain.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.util.Date;

@Entity
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue
    long id;
    String loginId;
    String pw;
    String name;
    String addr;
    String tel;

    @CreatedDate
    Date createdAt;

    @LastModifiedDate
    Date lastLoggedInAt;

    @Builder
    public User(String loginId, String pw, String name, String addr, String tel) {
        this.loginId = loginId;
        this.pw = pw;
        this.name = name;
        this.addr = addr;
        this.tel = tel;
    }
}
