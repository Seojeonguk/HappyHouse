package com.example.happyhouse.domain.entity;

import com.example.happyhouse.domain.dto.request.UserRegistrationReq;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

@Entity
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
@Getter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    @Getter
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

    @Getter
    @Enumerated(EnumType.STRING)
    private RoleType role;

    @Column(columnDefinition = "TIMESTAMP")
    @CreatedDate
    LocalDateTime createdAt;

    @Column(columnDefinition = "TIMESTAMP")
    @LastModifiedDate
    LocalDateTime lastLoggedInAt;

    @Builder
    public User(String loginId, String pw, String name, String addr, String tel, RoleType role) {
        this.loginId = loginId;
        this.pw = pw;
        this.name = name;
        this.addr = addr;
        this.tel = tel;
        this.role = RoleType.ROLE_USER;
    }

    public void updatePassword(PasswordEncoder passwordEncoder) {
        this.pw = passwordEncoder.encode(pw);
    }

    public void updateUser(UserRegistrationReq userRegistrationReq) {
        this.name = userRegistrationReq.getName();
        this.tel = userRegistrationReq.getTel();
        this.addr = userRegistrationReq.getAddr();
    }

    public boolean matchPassword(PasswordEncoder passwordEncoder, String password) {
        return passwordEncoder.matches(password, this.pw);
    }
}
