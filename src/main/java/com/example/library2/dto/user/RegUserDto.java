package com.example.library2.dto.user;

import com.example.library2.entity.user.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegUserDto {
    private String username;
    private String password;
    private String name;
    @Enumerated(EnumType.STRING)
    private Set<Role> roles;
}
