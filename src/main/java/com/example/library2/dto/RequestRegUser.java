package com.example.library2.dto;

import com.example.library2.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestRegUser {
    @NotBlank(message = "Username cannot be blank")
    @Size(min = 4, max = 100, message = "Username must be between 4 and 100 characters")
    private String username;
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[A-Za-z\\d]{6,}$", message = "Password must consist of at least 6 characters(mandatory to use lowercase,uppercase and 0-9 digital)!!!")
    private String password;
    @NotBlank(message = "Name cannot be blank")
    @Size(min = 1, max = 100, message = "Name must be between 1 and 100 characters")
    private String name;
    @Enumerated(EnumType.STRING)
    private Set<Role> roles;
}
