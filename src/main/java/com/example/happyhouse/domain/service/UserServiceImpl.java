package com.example.happyhouse.domain.service;

import com.example.happyhouse.domain.dto.request.LoginReq;
import com.example.happyhouse.domain.dto.request.UserRegistrationReq;
import com.example.happyhouse.domain.dto.response.TokenRes;
import com.example.happyhouse.domain.entity.RefreshToken;
import com.example.happyhouse.domain.entity.User;
import com.example.happyhouse.domain.repository.RefreshTokenRepository;
import com.example.happyhouse.domain.repository.UserRepository;
import com.example.happyhouse.util.Jwt;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final Jwt jwt;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenRepository refreshTokenRepository;

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
    public TokenRes login(LoginReq loginReq) {
        UsernamePasswordAuthenticationToken authenticationToken = loginReq.toAuthentication();

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        TokenRes tokenRes = jwt.generateToken(authentication);

        RefreshToken refreshToken = RefreshToken.builder()
                .key(authentication.getName())
                .value(tokenRes.getRefreshToken())
                .build();

        refreshTokenRepository.save(refreshToken);

        return tokenRes;
    }
}
