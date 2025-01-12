package com.example.library2.service;

import com.example.library2.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
public class SecurityService {
    private static final String USER_NOT_FOUND = "User not found!!!";
    @Autowired
    private UserService userService;

    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof String) {
            String currentUsername = (String) authentication.getPrincipal();
            Optional<User> currentUser = userService.findByUsername(currentUsername);
            return currentUser.orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, USER_NOT_FOUND));
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid authentication principal");
        }
    }
}