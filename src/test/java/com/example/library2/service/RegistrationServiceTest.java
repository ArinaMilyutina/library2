package com.example.library2.service;
import com.example.library2.dto.RequestRegUser;
import com.example.library2.dto.ResponseRegUser;
import com.example.library2.entity.User;
import com.example.library2.exception.AlreadyExistsException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class RegistrationServiceTest {
    private static final String USERNAME = "Arina20";
    private static final String PASSWORD = "Arina20";
    private static final String NAME = "Arina";

    @InjectMocks
    private RegistrationService registrationService;

    @Mock
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void regUser_ShouldCreateUser_WhenUsernameIsAvailable() {
        RequestRegUser requestRegUser = new RequestRegUser();
        requestRegUser.setUsername(USERNAME);
        requestRegUser.setPassword(PASSWORD);
        requestRegUser.setName(NAME);
        when(userService.createUser(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));
        ResponseRegUser response = registrationService.regUser(requestRegUser);
        verify(userService).checkUsernameAvailability(requestRegUser.getUsername());
        verify(userService).createUser(any(User.class));
        assertNotNull(response);
        assertEquals(USERNAME, response.getUsername());
        assertEquals(NAME, response.getName());
    }

}
