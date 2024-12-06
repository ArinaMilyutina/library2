package com.example.library2.service;

import com.example.library2.entity.user.User;
import com.example.library2.exception.AlreadyExistsException;
import com.example.library2.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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

    public void checkUsernameAvailability(String username) {
        Optional<User> byUsername = findByUsername(username);
        if (byUsername.isPresent()) {
            throw new AlreadyExistsException(USER_ALREADY_EXISTS);
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> byUsername = userRepository.findUserByUsername(username);
        if (byUsername.isPresent()) {
            return byUsername.get();
        }
        throw new UsernameNotFoundException(USER_NOT_FOUND);
    }
}
