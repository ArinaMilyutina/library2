package com.example.library2.controller;

import com.example.library2.controller.internal.InternalUserController;
import com.example.library2.dto.RequestAuthUser;
import com.example.library2.dto.RequestRegUser;
import com.example.library2.dto.ResponseAuthUser;
import com.example.library2.dto.ResponseRegUser;
import com.example.library2.entity.Role;
import com.example.library2.service.AuthenticationService;
import com.example.library2.service.RegistrationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.hamcrest.Matchers.is;

import java.util.Set;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class InternalUserControllerTest {
    @Mock
    private RegistrationService registrationService;
    @Mock
    private AuthenticationService authenticationService;

    @InjectMocks
    private InternalUserController userController;

    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private static final String USERNAME = "Arina20";
    private static final String PASSWORD = "Arina20";
    private static final String NAME = "Arina";
    private static final String REG_URL_USER = "/user/reg";
    private static final String REG_URL_ADMIN = "/user/reg/admin";
    private static final String LOGIN_URL = "/user/login";
    private static final String TOKEN = "token";


    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    void registrationUser() throws Exception {
        RequestRegUser userDto = createUserDto(Set.of(Role.USER));
        ResponseRegUser regUser = new ResponseRegUser();
        regUser.setName(userDto.getName());
        regUser.setUsername(userDto.getUsername());
        when(registrationService.regUser(userDto)).thenReturn(regUser);
        mockMvc.perform(post(REG_URL_USER)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username", is(USERNAME)));
    }

    @Test
    void registrationAdmin() throws Exception {
        RequestRegUser userDto = createUserDto(Set.of(Role.ADMIN));
        ResponseRegUser regUser = new ResponseRegUser();
        regUser.setName(userDto.getName());
        regUser.setUsername(userDto.getUsername());
        when(registrationService.regUser(userDto)).thenReturn(regUser);
        mockMvc.perform(post(REG_URL_ADMIN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username", is(USERNAME)));
    }

    @Test
    void loginSuccessful() throws Exception {
        RequestAuthUser authUserDto = new RequestAuthUser();
        authUserDto.setUsername(USERNAME);
        authUserDto.setPassword(PASSWORD);
        ResponseAuthUser responseAuthUser = new ResponseAuthUser();
        responseAuthUser.setToken(TOKEN);
        when(authenticationService.authenticate(authUserDto)).thenReturn(responseAuthUser);
        mockMvc.perform(post(LOGIN_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authUserDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token", is(TOKEN)));
    }

    private RequestRegUser createUserDto(Set<Role> roles) {
        RequestRegUser userDto = new RequestRegUser();
        userDto.setUsername(USERNAME);
        userDto.setPassword(PASSWORD);
        userDto.setName(NAME);
        userDto.setRoles(roles);
        return userDto;
    }
}