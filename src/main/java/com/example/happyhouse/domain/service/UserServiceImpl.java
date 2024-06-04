package com.example.happyhouse.domain.service;

import com.example.happyhouse.domain.dto.request.LoginReq;
import com.example.happyhouse.domain.dto.request.RefreshReq;
import com.example.happyhouse.domain.dto.request.UserRegistrationReq;
import com.example.happyhouse.domain.dto.response.TokenRes;
import com.example.happyhouse.domain.dto.response.UserInfoRes;
import com.example.happyhouse.domain.entity.RefreshToken;
import com.example.happyhouse.domain.entity.User;
import com.example.happyhouse.domain.repository.RefreshTokenRepository;
import com.example.happyhouse.domain.repository.UserRepository;
import com.example.happyhouse.security.CustomUserDetailService;
import com.example.happyhouse.security.CustomUserDetails;
import com.example.happyhouse.util.Jwt;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
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
    private final CustomUserDetailService customUserDetailService;

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

    @Override
    public UserInfoRes getMyInfo(Authentication authentication) {
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getDetails();
        User user = customUserDetails.getUser();
        return new UserInfoRes(user);
    }

    @Override
    @Transactional
    public void deleteUser(Authentication authentication) {
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getDetails();
        User user = customUserDetails.getUser();
        userRepository.delete(user);
    }

    @Override
    public TokenRes refresh(RefreshReq refreshReq) {
        String refreshToken = refreshReq.getRefreshToken();
        if (!jwt.validateToken(refreshToken)) {
            throw new BadCredentialsException(refreshToken + " is not valid");
        }

        String loginId = jwt.getLoginId(refreshToken);
        UserDetails userDetails = customUserDetailService.loadUserByUsername(loginId);
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

        return jwt.generateToken(usernamePasswordAuthenticationToken);
    }

    @Override
    @Transactional
    public void modifyUser(Authentication authentication, UserRegistrationReq userRegistrationReq) {
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getDetails();
        User user = customUserDetails.getUser();
        user.updateUser(userRegistrationReq);
        userRepository.save(user);
    }
}
