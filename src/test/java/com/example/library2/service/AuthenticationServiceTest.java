package com.example.library2.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.example.library2.dto.RequestAuthUser;
import com.example.library2.dto.ResponseAuthUser;
import com.example.library2.entity.User;
import com.example.library2.exception.IncorrectPasswordException;
import com.example.library2.exception.NotFoundException;
import com.example.library2.jwt.JWTTokenProvider;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class AuthenticationServiceTest {
    private static final String USERNAME = "testUser";
    private static final String INCORRECT_PASSWORD = "Incorrect password!!!";
    private static final String USER_NOT_FOUND = "User not found!!!";

    private static final String PASSWORD = "password";
    private static final String TOKEN = "token";
    @InjectMocks
    private AuthenticationService authenticationService;
    @Mock
    private UserService userService;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JWTTokenProvider jwtTokenProvider;

    @Test
    public void authenticate_UserFoundAndPasswordMatches_ReturnsToken() throws NotFoundException {
        RequestAuthUser authUserDto = new RequestAuthUser(USERNAME, PASSWORD);
        User user = User.builder()
                .username(USERNAME)
                .password(PASSWORD)
                .build();

        when(userService.findByUsername(USERNAME)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(PASSWORD, user.getPassword())).thenReturn(true);
        when(jwtTokenProvider.generateToken(user)).thenReturn(TOKEN);
        ResponseAuthUser response = authenticationService.authenticate(authUserDto);
        assertNotNull(response);
        assertEquals(TOKEN, response.getToken());
        verify(userService).findByUsername(USERNAME);
        verify(passwordEncoder).matches(PASSWORD, user.getPassword());
        verify(jwtTokenProvider).generateToken(user);
    }

    @Test
    public void authenticate_UserNotFound_ThrowsNotFoundException() {
        RequestAuthUser authUserDto = new RequestAuthUser(USERNAME, PASSWORD);
        when(userService.findByUsername(USERNAME)).thenReturn(Optional.empty());
        NotFoundException thrown = assertThrows(NotFoundException.class, () -> {
            authenticationService.authenticate(authUserDto);
        });

        assertEquals(USER_NOT_FOUND, thrown.getMessage());
        verify(userService).findByUsername(USERNAME);
        verifyNoInteractions(passwordEncoder, jwtTokenProvider);
    }

    @Test
    public void authenticate_PasswordDoesNotMatch_ThrowsIncorrectPasswordException() {
        RequestAuthUser authUserDto = new RequestAuthUser(USERNAME, PASSWORD);
        User user = new User();
        when(userService.findByUsername(USERNAME)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(PASSWORD, user.getPassword())).thenReturn(false);
        IncorrectPasswordException thrown = assertThrows(IncorrectPasswordException.class, () -> {
            authenticationService.authenticate(authUserDto);
        });

        assertEquals(INCORRECT_PASSWORD, thrown.getMessage());
        verify(userService).findByUsername(USERNAME);
        verify(passwordEncoder).matches(PASSWORD, user.getPassword());
        verifyNoInteractions(jwtTokenProvider);
    }
}
