package com.example.library2.controller.internal;

import com.example.library2.dto.AuthUserDto;
import com.example.library2.dto.RegUserDto;
import com.example.library2.entity.Role;
import com.example.library2.entity.User;
import com.example.library2.exception.NotFoundException;
import com.example.library2.service.AuthenticationService;
import com.example.library2.service.RegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Set;

@RestController
@RequestMapping("/user")
public class InternalUserController {
    @Autowired
    private AuthenticationService authenticationService;
    @Autowired
    private RegistrationService registrationService;

    @PostMapping("/reg")
    public ResponseEntity<User> registrationUser(@Valid @RequestBody RegUserDto userDto) {
        userDto.setRoles(Set.of(Role.USER));
        return ResponseEntity.ok(registrationService.regUser(userDto));
    }

    @PostMapping("/reg/admin")
    public ResponseEntity<User> registrationAdmin(@Valid @RequestBody RegUserDto userDto) {
        userDto.setRoles(Set.of(Role.ADMIN));
        return ResponseEntity.ok(registrationService.regUser(userDto));
    }

    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestBody AuthUserDto authUserDto) throws NotFoundException {
        String token = authenticationService.authenticate(authUserDto);
        return ResponseEntity.ok(token);
    }
}