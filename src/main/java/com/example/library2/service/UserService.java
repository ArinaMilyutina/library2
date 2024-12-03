package com.example.library2.service;

import com.example.library2.dto.user.RegUserDto;
import com.example.library2.entity.user.User;
import com.example.library2.exception.UsernameAlreadyExistsException;
import com.example.library2.mapper.UserMapper;
import com.example.library2.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService implements UserDetailsService {
    private static final String USER_NOT_FOUND = "User not found: ";
    private static final String USER_ALREADY_EXISTS = "Username already exists";

    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Autowired
    private UserRepository userRepository;

    public User createUser(RegUserDto userDto) {
        try {
            User user = UserMapper.INSTANCE.regUserDtoToUser(userDto);
            user.setPassword(passwordEncoder().encode(user.getPassword()));
            return userRepository.save(user);
        } catch (DataIntegrityViolationException e) {
            throw new UsernameAlreadyExistsException(USER_ALREADY_EXISTS);
        }

    }

    public Optional<User> findByUsername(String username) {
        return Optional.ofNullable(userRepository.findUserByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(USER_NOT_FOUND + username)));
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
