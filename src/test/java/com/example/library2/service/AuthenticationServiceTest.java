package com.example.library2.service;

import com.example.library2.dto.user.AuthUserDto;
import com.example.library2.exception.IncorrectPasswordException;
import com.example.library2.exception.NotFoundException;
import com.example.library2.jwt.JWTTokenProvider;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.library2.entity.user.User;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthenticationServiceTest {
    @InjectMocks
    private AuthenticationService authenticationService;

    @Mock
    private UserService userService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JWTTokenProvider jwtTokenProvider;

    private static final AuthUserDto authUserDto = new AuthUserDto();
    private static final String INCORRECT_PASSWORD = "Incorrect password!!!";
    private static final String USER_NOT_FOUND = "User not found!!!";
    private static final String expectedToken = "some.jwt.token";

    private static final User user = new User();

    @Test
    public void authenticate_UserNotFound_ThrowsNotFoundException() {
        when(userService.findByUsername(authUserDto.getUsername())).thenReturn(Optional.empty());
        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            authenticationService.authenticate(authUserDto);
        });
        assertEquals(USER_NOT_FOUND, exception.getMessage());
    }

    @Test
    public void authenticate_IncorrectPassword_ThrowsIncorrectPasswordException() {
        when(userService.findByUsername(authUserDto.getUsername())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(authUserDto.getPassword(), user.getPassword())).thenReturn(false);
        IncorrectPasswordException exception = assertThrows(IncorrectPasswordException.class, () -> {
            authenticationService.authenticate(authUserDto);
        });
        assertEquals(INCORRECT_PASSWORD, exception.getMessage());
    }

    @Test
    public void authenticate_SuccessfulAuthentication_ReturnsToken() throws NotFoundException {
        when(userService.findByUsername(authUserDto.getUsername())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(authUserDto.getPassword(), user.getPassword())).thenReturn(true);
        when(jwtTokenProvider.generateToken(authUserDto.getUsername(), user.getRoles())).thenReturn(expectedToken);
        String actualToken = authenticationService.authenticate(authUserDto);
        assertEquals(expectedToken, actualToken);
    }
}

