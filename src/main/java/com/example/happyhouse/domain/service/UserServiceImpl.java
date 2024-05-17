package com.example.happyhouse.domain.service;

import com.example.happyhouse.domain.dto.request.LoginReq;
import com.example.happyhouse.domain.dto.request.UserRegistrationReq;
import com.example.happyhouse.domain.entity.User;
import com.example.happyhouse.domain.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public String sigunUp(UserRegistrationReq userRegistrationReq) {
        if (userRepository.existsByLoginId(userRegistrationReq.getLoginId())) {
            throw new IllegalArgumentException(userRegistrationReq.getLoginId() + " already exists");
        }

        User user = userRegistrationReq.toEntity();
        user.updatePassword(passwordEncoder);
        userRepository.save(user);

        return userRegistrationReq.getLoginId();
    }

    @Override
    public String login(LoginReq loginReq) {
        User users = userRepository.findByLoginId(loginReq.getLoginId())
                .filter(user -> user.matchPassword(passwordEncoder, loginReq.getPw()))
                .orElseThrow(() -> new IllegalArgumentException("ID or password incorrect"));

        return loginReq.getLoginId();
    }
}
