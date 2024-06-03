package com.example.happyhouse.security;

import com.example.happyhouse.domain.entity.User;
import com.example.happyhouse.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userRepository.findByLoginId(username)
                .orElseThrow(() -> new UsernameNotFoundException(username + " : not found user name"));

        return new CustomUserDetails(user);
    }
}
