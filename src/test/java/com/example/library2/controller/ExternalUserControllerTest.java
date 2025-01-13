package com.example.library2.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.example.library2.controller.external.ExternalUserController;
import com.example.library2.dto.UserInfoResponse;
import com.example.library2.entity.User;
import com.example.library2.service.SecurityService;
import com.example.library2.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.security.test.context.support.WithMockUser;

public class ExternalUserControllerTest {
    private static final String URL_GET_CURRENT_USER = "/external/user/current-user";
    private static final String URL_FIND_BY_ID = "/external/user/find-by-id/{id}";
    private static final Long ID = 1L;

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private SecurityService securityService;

    @Mock
    private UserService userService;

    @InjectMocks
    private ExternalUserController externalUserController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(externalUserController).build();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getCurrentUserUser() throws Exception {
        User mockUser = new User();
        mockUser.setId(ID);
        when(securityService.getCurrentUser()).thenReturn(mockUser);
        mockMvc.perform(get(URL_GET_CURRENT_USER)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(ID.toString()));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void findById() throws Exception {
        UserInfoResponse mockResponse = new UserInfoResponse();
        mockResponse.setUserId(ID);
        when(userService.takeTheBook(ID)).thenReturn(mockResponse);
        mockMvc.perform(get(URL_FIND_BY_ID, ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(ID));
    }

}


