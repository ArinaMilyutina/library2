package com.example.library2.service;

import com.example.library2.dto.RequestRegUser;
import com.example.library2.dto.ResponseRegUser;
import com.example.library2.entity.User;
import com.example.library2.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class RegistrationService {
    @Autowired
    private UserService userService;

    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    public ResponseRegUser regUser(RequestRegUser regUserDto) {
        User user = UserMapper.INSTANCE.regUserDtoToUser(regUserDto);
        userService.checkUsernameAvailability(user.getUsername());
        user.setPassword(passwordEncoder().encode(user.getPassword()));
        userService.createUser(user);

        return ResponseRegUser.builder()
                .username(user.getUsername())
                .name(user.getName())
                .build();
    }
}