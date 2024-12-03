package com.example.library2.controller;

import com.example.library2.dto.user.AuthUserDto;
import com.example.library2.dto.user.RegUserDto;
import com.example.library2.entity.user.Role;
import com.example.library2.entity.user.User;
import com.example.library2.exception.NotFoundException;
import com.example.library2.service.AuthenticationService;
import com.example.library2.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Set;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping("/reg")
    public ResponseEntity<User> registrationUser(@Valid @RequestBody RegUserDto userDto) throws NotFoundException {
        userDto.setRoles(Set.of(Role.USER));
        return ResponseEntity.ok(userService.createUser(userDto));
    }

    @PostMapping("/reg/admin")
    public ResponseEntity<User> registrationAdmin(@Valid @RequestBody RegUserDto userDto) throws NotFoundException {
        userDto.setRoles(Set.of(Role.ADMIN));
        return ResponseEntity.ok(userService.createUser(userDto));
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody AuthUserDto authUserDto) throws NotFoundException {
        String token = authenticationService.authenticate(authUserDto);
        return ResponseEntity.ok(token);
    }
}
