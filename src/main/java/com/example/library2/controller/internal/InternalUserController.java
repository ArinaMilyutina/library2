package com.example.library2.controller.internal;

import com.example.library2.dto.RequestAuthUser;
import com.example.library2.dto.RequestRegUser;
import com.example.library2.dto.ResponseAuthUser;
import com.example.library2.dto.ResponseRegUser;
import com.example.library2.entity.Role;
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
    public ResponseEntity<ResponseRegUser> registrationUser(@Valid @RequestBody RequestRegUser userDto) {
        userDto.setRoles(Set.of(Role.USER));
        return ResponseEntity.ok(registrationService.regUser(userDto));
    }

    @PostMapping("/reg/admin")
    public ResponseEntity<ResponseRegUser> registrationAdmin(@Valid @RequestBody RequestRegUser userDto) {
        userDto.setRoles(Set.of(Role.ADMIN));
        return ResponseEntity.ok(registrationService.regUser(userDto));
    }

    @PostMapping("/login")
    public ResponseEntity<ResponseAuthUser> loginUser(@RequestBody RequestAuthUser authUserDto) throws NotFoundException {
        return ResponseEntity.ok(authenticationService.authenticate(authUserDto));
    }
}