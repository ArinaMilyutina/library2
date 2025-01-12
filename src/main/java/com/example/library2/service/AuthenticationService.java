package com.example.library2.service;

import com.example.library2.dto.RequestAuthUser;
import com.example.library2.dto.ResponseAuthUser;
import com.example.library2.entity.User;
import com.example.library2.exception.IncorrectPasswordException;
import com.example.library2.exception.NotFoundException;
import com.example.library2.jwt.JWTTokenProvider;
import com.example.library2.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthenticationService {
    private static final String INCORRECT_PASSWORD = "Incorrect password!!!";
    private static final String USER_NOT_FOUND = "User not found!!!";

    @Autowired
    private UserService userService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JWTTokenProvider jwtTokenProvider;

    public ResponseAuthUser authenticate(RequestAuthUser authUserDto) throws NotFoundException {
        User userToAuthenticate = UserMapper.INSTANCE.loginUserDtoToUser(authUserDto);
        Optional<User> byUsername = userService.findByUsername(userToAuthenticate.getUsername());
        if (byUsername.isEmpty()) {
            throw new NotFoundException(USER_NOT_FOUND);
        }
        User user = byUsername.get();
        if (passwordEncoder.matches(authUserDto.getPassword(), user.getPassword())) {
            return ResponseAuthUser.builder().token(jwtTokenProvider.generateToken(user)).build();
        }
        throw new IncorrectPasswordException(INCORRECT_PASSWORD);
    }
}

