package com.example.library2.controller;

import com.example.library2.dto.BookDto;
import com.example.library2.entity.book.Book;
import com.example.library2.entity.book.Genre;
import com.example.library2.service.BookService;
import com.example.library2.service.SecurityService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;
import java.util.Optional;
import java.util.Set;

import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class BookControllerTest {
    @InjectMocks
    private BookController bookController;

    @Mock
    private BookService bookService;

    @Mock
    private SecurityService securityService;

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(bookController).build();
        objectMapper = new ObjectMapper();
    }

    private static final String URL_CREATE_BOOK = "/book/admin/create";
    private static final String URL_FIND_ALL = "/book/books";
    private static final String URL_FIND_BY_ID = "/book/{id}";
    private static final String URL_FIND_BY_ISBN = "/book/admin/ISBN/{ISBN}";
    private static final String URL_DELETE_BY_ISBN = "/book/admin/delete/{ISBN}";
    private static final String URL_UPDATE_BY_ISBN = "/book/admin/update/{ISBN}";
    private static final String TITLE = "The Little Prince";
    private static final String AUTHOR = "Antoine de Saint-Exupery";
    private static final String ISBN = "1234567890123";
    private static final String DESCRIPTION = "The fairy tale tells about a Little Prince who visit various planets in space, including Earth";

    @WithMockUser(roles = "ADMIN")
    @Test
    void createBookTest() throws Exception {
        BookDto bookDto = createBookDto();
        Book book = new Book();
        when(bookService.createBook(any(BookDto.class))).thenReturn(book);
        mockMvc.perform(post(URL_CREATE_BOOK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookDto)))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(book)));
    }

    @Test
    void findAllTest() throws Exception {
        Book book1 = new Book();
        Book book2 = new Book();
        when(bookService.findAll()).thenReturn(Arrays.asList(book1, book2));
        mockMvc.perform(get(URL_FIND_ALL))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void findByIdTest() throws Exception {
        Long id = 1L;
        Book book = new Book();
        when(bookService.findById(id)).thenReturn(Optional.of(book));
        mockMvc.perform(get(URL_FIND_BY_ID, id))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @WithMockUser(roles = "ADMIN")
    @Test
    void findByISBNTest() throws Exception {
        Book book = new Book();
        when(bookService.findByISBN(ISBN)).thenReturn(Optional.of(book));
        mockMvc.perform(get(URL_FIND_BY_ISBN, ISBN))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @WithMockUser(roles = "ADMIN")
    @Test
    void deleteByISBNTest() throws Exception {
        when(bookService.deleteBookByISBN(ISBN)).thenReturn("Book deleted");
        mockMvc.perform(delete(URL_DELETE_BY_ISBN, ISBN))
                .andExpect(status().isOk())
                .andExpect(content().string("Book deleted"));
    }

    @WithMockUser(roles = "ADMIN")
    @Test
    void updateByISBNTest() throws Exception {
        BookDto bookDto = createBookDto();
        Book createdBook = new Book();
        when(bookService.updateBookByISBN(eq(ISBN), any(BookDto.class))).thenReturn(createdBook);
        mockMvc.perform(put(URL_UPDATE_BY_ISBN, ISBN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }
    private BookDto createBookDto() {
        BookDto bookDto = new BookDto();
        bookDto.setTitle(TITLE);
        bookDto.setAuthor(AUTHOR);
        bookDto.setISBN(ISBN);
        bookDto.setDescription(DESCRIPTION);
        bookDto.setGenre(Set.of(Genre.FANTASY, Genre.TALE));
        return bookDto;
    }
}