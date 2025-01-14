package com.example.library2.service;

import com.example.library2.dto.UserInfoResponse;
import com.example.library2.entity.User;
import com.example.library2.exception.AlreadyExistsException;
import com.example.library2.exception.NotFoundException;
import com.example.library2.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class UserService implements UserDetailsService {
    private static final String USER_NOT_FOUND = "User not found !!!";
    private static final String USER_ALREADY_EXISTS = "Username already exists!!!";

    @Autowired
    private UserRepository userRepository;

    public User createUser(User user) {
        return userRepository.save(user);
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findUserByUsername(username);
    }

    public UserInfoResponse takeTheBook(Long id) throws NotFoundException {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isEmpty()) {
            throw new NotFoundException(USER_NOT_FOUND);
        }
        User user = userOptional.get();
        return UserInfoResponse.builder()
                .userId(user.getId())
                .username(user.getUsername())
                .build();
    }

    public void checkUsernameAvailability(String username) {
        Optional<User> byUsername = findByUsername(username);
        if (byUsername.isPresent()) {
            throw new AlreadyExistsException(USER_ALREADY_EXISTS);
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        Optional<User> byUsername = userRepository.findUserByUsername(username);
        if (byUsername.isPresent()) {
            return byUsername.get();
        }
        throw new NotFoundException(USER_NOT_FOUND);
    }
}
