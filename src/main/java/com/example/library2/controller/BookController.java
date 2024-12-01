package com.example.library2.controller;

import com.example.library2.dto.BookDto;
import com.example.library2.entity.book.Book;
import com.example.library2.service.BookService;
import com.example.library2.service.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;


@RestController
@RequestMapping("/book")
public class BookController {
    @Autowired
    private BookService bookService;
    @Autowired
    private SecurityService securityService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping()
    public ResponseEntity<Book> createBook(@Valid @RequestBody BookDto bookDto) {
        bookDto.setAdmin(securityService.getCurrentUser());
        return ResponseEntity.status(HttpStatus.CREATED).body(bookService.createBook(bookDto));
    }
}
