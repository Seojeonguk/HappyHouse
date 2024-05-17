package com.example.happyhouse.domain.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Entity
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    @Column(unique = true, nullable = false, length = 20)
    String loginId;

    @Column(nullable = false, length = 60)
    String pw;

    @Column(length = 10)
    String name;

    @Column(length = 300)
    String addr;

    @Column(length = 13)
    String tel;

    @Column(columnDefinition = "TIMESTAMP")
    @CreatedDate
    LocalDateTime createdAt;

    @Column(columnDefinition = "TIMESTAMP")
    @LastModifiedDate
    LocalDateTime lastLoggedInAt;

    @Builder
    public User(String loginId, String pw, String name, String addr, String tel) {
        this.loginId = loginId;
        this.pw = pw;
        this.name = name;
        this.addr = addr;
        this.tel = tel;
    }

    public void updatePassword(PasswordEncoder passwordEncoder) {
        this.pw = passwordEncoder.encode(pw);
    }
}
