package com.example.library2.service;

import com.example.library2.dto.user.RegUserDto;
import com.example.library2.entity.user.User;
import com.example.library2.exception.AlreadyExistsException;
import com.example.library2.exception.NotFoundException;
import com.example.library2.mapper.UserMapper;
import com.example.library2.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService implements UserDetailsService {
    private static final String USER_NOT_FOUND = "User not found ";
    private static final String USER_ALREADY_EXISTS = "Username already exists!!!";

    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Autowired
    private UserRepository userRepository;

    public User createUser(RegUserDto userDto) throws NotFoundException {
        User user = UserMapper.INSTANCE.regUserDtoToUser(userDto);
        Optional<User> byUsername = findByUsername(user.getUsername());
        if (byUsername.isPresent()) {
            throw new AlreadyExistsException(USER_ALREADY_EXISTS);
        }
        user.setPassword(passwordEncoder().encode(user.getPassword()));
        return userRepository.save(user);
    }

    public Optional<User> findByUsername(String username) throws NotFoundException {
        Optional<User> user = userRepository.findUserByUsername(username);
        if (user.isEmpty()) {
            throw new NotFoundException(USER_NOT_FOUND);
        }
        return userRepository.findUserByUsername(username);
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
