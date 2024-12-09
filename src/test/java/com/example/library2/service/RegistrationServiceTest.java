package com.example.library2.service;

import com.example.library2.dto.user.RegUserDto;
import com.example.library2.entity.user.User;
import com.example.library2.exception.AlreadyExistsException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RegistrationServiceTest {
    @InjectMocks
    private RegistrationService registrationService;
    @Mock
    private UserService userService;
    private static final String USER_ALREADY_EXISTS = "Username is already in use!";
    private static final String USERNAME = "Arina20";
    private static final String PASSWORD = "Arina20";


    private BCryptPasswordEncoder passwordEncoder;

    @BeforeEach
    public void setUp() {
        passwordEncoder = registrationService.passwordEncoder();
    }

    @Test
    public void regUserTest_ReturnUser() {
        RegUserDto regUserDto = new RegUserDto();
        regUserDto.setUsername(USERNAME);
        regUserDto.setPassword(PASSWORD);
        User user = new User();
        user.setUsername(regUserDto.getUsername());
        String encodedPassword = passwordEncoder.encode(regUserDto.getPassword());
        user.setPassword(encodedPassword);
        doNothing().when(userService).checkUsernameAvailability(user.getUsername());
        when(userService.createUser(any(User.class))).thenReturn(user);
        User regUser = registrationService.regUser(regUserDto);
        assertEquals(USERNAME, regUser.getUsername());
        assertEquals(encodedPassword, regUser.getPassword());
        verify(userService).checkUsernameAvailability(user.getUsername());
        verify(userService).createUser(any(User.class));
    }


    @Test
    public void regUserTest_AlreadyExistException() {
        User user = new User();
        RegUserDto regUserDto = new RegUserDto();
        doThrow(new AlreadyExistsException(USER_ALREADY_EXISTS))
                .when(userService).checkUsernameAvailability(user.getUsername());

        AlreadyExistsException exception = assertThrows(AlreadyExistsException.class, () -> {
            registrationService.regUser(regUserDto);
        });

        assertEquals(USER_ALREADY_EXISTS, exception.getMessage());
    }

    @Test
    void passwordEncoderTest() {
        BCryptPasswordEncoder encoder = registrationService.passwordEncoder();
        assertNotNull(encoder);
        String encoded = encoder.encode(PASSWORD);
        assertNotEquals(PASSWORD, encoded);
    }
}

