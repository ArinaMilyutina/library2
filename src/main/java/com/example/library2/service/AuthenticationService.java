package com.example.library2.service;

import com.example.library2.dto.user.AuthUserDto;
import com.example.library2.entity.user.User;
import com.example.library2.jwt.JWTTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthenticationService {
    @Autowired
    private UserService userService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JWTTokenProvider jwtTokenProvider;

    public String authenticate(AuthUserDto authUserDto) {

        Optional<User> byUsername = userService.findByUsername(authUserDto.getUsername());
        if (byUsername.isEmpty()) {
            return "Incorrect data entered!!!";
        }
        User user = byUsername.get();
        if (passwordEncoder.matches(authUserDto.getPassword(), user.getPassword())) {
            return jwtTokenProvider.generateToken(authUserDto.getUsername(), user.getRoles());
        }

        return "Incorrect data entered!!!";
    }
}

