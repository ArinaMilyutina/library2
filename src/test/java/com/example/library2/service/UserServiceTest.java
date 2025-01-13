package com.example.library2.service;

import com.example.library2.dto.UserInfoResponse;
import com.example.library2.entity.User;
import com.example.library2.exception.AlreadyExistsException;
import com.example.library2.exception.NotFoundException;
import com.example.library2.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User testUser;
    private static final String TEST_USER = "testUser";
    private static final String USER_NOT_FOUND = "User not found !!!";
    private static final Long USER_ID = 1L;

    private static final String AVAILABLE_USER = "availableUser";
    private static final String EXISTING_USER = "existingUser";

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        testUser = new User();
        testUser.setUsername(TEST_USER);
    }

    @Test
    public void createUserTest() {
        when(userRepository.save(testUser)).thenReturn(testUser);
        User savedUser = userService.createUser(testUser);
        assertNotNull(savedUser);
        assertEquals(TEST_USER, savedUser.getUsername());
        verify(userRepository).save(testUser);
    }

    @Test
    public void findByUsernameTest() {
        when(userRepository.findUserByUsername(TEST_USER)).thenReturn(Optional.of(testUser));
        Optional<User> foundUser = userService.findByUsername(TEST_USER);
        assertTrue(foundUser.isPresent());
        assertEquals(TEST_USER, foundUser.get().getUsername());
    }

    @Test
    public void checkUsernameAvailability_usernameAvailable() {
        when(userRepository.findUserByUsername(AVAILABLE_USER)).thenReturn(Optional.empty());
        userService.checkUsernameAvailability(AVAILABLE_USER);
    }

    @Test
    public void checkUsernameAvailability_usernameExists() {
        when(userRepository.findUserByUsername(EXISTING_USER)).thenReturn(Optional.of(testUser));
        assertThrows(AlreadyExistsException.class, () -> {
            userService.checkUsernameAvailability(EXISTING_USER);
        });
    }

    @Test
    public void takeTheBookTest_UserFound() {
        User user = new User();
        user.setId(USER_ID);
        user.setUsername(TEST_USER);
        when(userRepository.findById(USER_ID)).thenReturn(Optional.of(user));
        UserInfoResponse response = userService.takeTheBook(USER_ID);
        assertNotNull(response);
        assertEquals(USER_ID, response.getUserId());
        assertEquals(TEST_USER, response.getUsername());
    }

    @Test
    public void testTakeTheBook_UserNotFound() {
        when(userRepository.findById(USER_ID)).thenReturn(Optional.empty());
        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            userService.takeTheBook(USER_ID);
        });

        assertEquals(USER_NOT_FOUND, exception.getMessage());
    }


}
