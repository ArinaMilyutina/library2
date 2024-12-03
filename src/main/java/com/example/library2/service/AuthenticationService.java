package com.example.library2.service;

import com.example.library2.dto.user.AuthUserDto;
import com.example.library2.entity.user.User;
import com.example.library2.jwt.JWTTokenProvider;
import com.example.library2.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthenticationService {
    private static final String INCORRECT_DATA="Incorrect data entered!!!";
    @Autowired
    private UserService userService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JWTTokenProvider jwtTokenProvider;

    public String authenticate(AuthUserDto authUserDto) {
        User userToAuthenticate= UserMapper.INSTANCE.loginUserDtoToUser(authUserDto);
        Optional<User> byUsername = userService.findByUsername(userToAuthenticate.getUsername());
        if (byUsername.isEmpty()) {
            return INCORRECT_DATA;
        }
        User user = byUsername.get();
        if (passwordEncoder.matches(authUserDto.getPassword(), user.getPassword())) {
            return jwtTokenProvider.generateToken(authUserDto.getUsername(), user.getRoles());
        }

        return INCORRECT_DATA;
    }
}

