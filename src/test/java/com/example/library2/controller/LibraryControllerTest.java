package com.example.library2.controller;

import com.example.library2.dto.library.LibraryDto;
import com.example.library2.entity.Library;


import com.example.library2.entity.book.Book;
import com.example.library2.service.LibraryService;
import com.example.library2.service.SecurityService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.Arrays;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class LibraryControllerTest {
    @InjectMocks
    private LibraryController libraryController;

    @Mock
    private LibraryService libraryService;

    @Mock
    private SecurityService securityService;

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
    private static final String ISBN = "1234567890123";
    private static final String USERNAME = "Arina20";
    private static final String URL_CREATE_LIBRARY = "/library/admin/create";
    private static final String URL_GET_AVAILABLE_BOOKS = "/library/available-books";


    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(libraryController).build();
    }

    @WithMockUser(roles = "ADMIN")
    @Test
    void createLibraryTest() throws Exception {
        LibraryDto libraryDto = new LibraryDto();
        libraryDto.setReturnDate(LocalDateTime.of(2025, 3, 15, 14, 30));
        Library library = new Library();
        library.setReturnDate(libraryDto.getReturnDate());
        when(libraryService.createLibrary(eq(USERNAME), eq(ISBN), any(LibraryDto.class))).thenReturn(library);
        mockMvc.perform(post(URL_CREATE_LIBRARY)
                        .param("username", USERNAME)
                        .param("isbn", ISBN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(libraryDto)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(library)));
    }


    @Test
    public void getAvailableBooksTest() throws Exception {
        Book book1 = new Book();
        Book book2 = new Book();
        when(libraryService.getAvailableBooks()).thenReturn(Arrays.asList(book1, book2));
        mockMvc.perform(get(URL_GET_AVAILABLE_BOOKS))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(2));
    }

}
