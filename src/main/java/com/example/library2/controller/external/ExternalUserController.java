package com.example.library2.controller.external;

import com.example.library2.dto.UserInfoResponse;
import com.example.library2.entity.User;
import com.example.library2.exception.NotFoundException;
import com.example.library2.service.SecurityService;
import com.example.library2.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/external/user")
public class ExternalUserController {

    @Autowired
    private SecurityService securityService;
    @Autowired
    private UserService userService;

    @PreAuthorize(value = "hasRole('ADMIN')")
    @GetMapping("/current-user")
    public ResponseEntity<Long> getCurrentUser() {
        User currentUser = securityService.getCurrentUser();
        return ResponseEntity.ok(currentUser.getId());
    }

    @PreAuthorize(value = "hasRole('ADMIN')")
    @GetMapping("/find-by-id/{id}")
    public ResponseEntity<UserInfoResponse> findById(@PathVariable Long id) throws NotFoundException {
        return ResponseEntity.ok(userService.takeTheBook(id));
    }

}
