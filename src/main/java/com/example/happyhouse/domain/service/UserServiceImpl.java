package com.example.happyhouse.domain.service;

import com.example.happyhouse.domain.dto.request.UserRegistrationReq;
import com.example.happyhouse.domain.entity.User;
import com.example.happyhouse.domain.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    @Transactional
    public String sigunUp(UserRegistrationReq userRegistrationReq) {
        if (userRepository.existsByLoginId(userRegistrationReq.getLoginId())) {
            throw new IllegalArgumentException(userRegistrationReq.getLoginId() + " already exists");
        }

        User user = userRegistrationReq.toEntity();
        userRepository.save(user);

        return userRegistrationReq.getLoginId();
    }
}
