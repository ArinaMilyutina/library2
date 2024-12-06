package com.example.library2.service;

import com.example.library2.dto.user.RegUserDto;
import com.example.library2.entity.user.User;
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

    public User regUser(RegUserDto regUserDto) {
        User user = UserMapper.INSTANCE.regUserDtoToUser(regUserDto);
        userService.checkUsernameAvailability(user.getUsername());
        user.setPassword(passwordEncoder().encode(user.getPassword()));
        return userService.createUser(user);
    }
}