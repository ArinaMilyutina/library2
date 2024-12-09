package com.example.library2.service;

import com.example.library2.entity.user.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SecurityServiceTest {

    @InjectMocks
    private SecurityService securityService;

    @Mock
    private UserService userService;

    @Mock
    private Authentication authentication;

    @Mock
    private UserDetails userDetails;

    private static final User user=new User();
    private static final String USERNAME = "Arina20";
    private static final String USER_NOT_FOUND = "User not found!!!";

    @Test
    public void getCurrentUser_UserExists_ReturnsUser() {
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn(USERNAME);
        when(userService.findByUsername(USERNAME)).thenReturn(Optional.of(user));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        User returnedUser = securityService.getCurrentUser();
        assertEquals(user, returnedUser);
    }

    @Test
    public void getCurrentUser_UserNotFound_ThrowsResponseStatusException() {
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn(USERNAME);
        when(userService.findByUsername(USERNAME)).thenReturn(Optional.empty());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            securityService.getCurrentUser();
        });
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getStatus());
        assertEquals(USER_NOT_FOUND, exception.getReason());
    }
}
