package com.example.library2.controller;

import com.example.library2.dto.book.BookDto;
import com.example.library2.entity.book.Book;
import com.example.library2.exception.NotFoundException;
import com.example.library2.service.BookService;
import com.example.library2.service.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;


@RestController
@RequestMapping("/book")
public class BookController {
    @Autowired
    private BookService bookService;
    @Autowired
    private SecurityService securityService;

    @PreAuthorize(value = "hasRole('ADMIN')")
    @PostMapping("/admin/create")
    public ResponseEntity<Book> createBook(@Valid @RequestBody BookDto bookDto) {
        bookDto.setAdmin(securityService.getCurrentUser());
        return ResponseEntity.status(HttpStatus.CREATED).body(bookService.createBook(bookDto));
    }

    @GetMapping("/books")
    public ResponseEntity<List<Book>> findAll() throws NotFoundException {
        return ResponseEntity.of(Optional.of(bookService.findAll()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Book> findById(@PathVariable Long id) throws NotFoundException {
        Optional<Book> book = bookService.findById(id);
        return book
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin/ISBN/{ISBN}")
    public ResponseEntity<Book> findByISBN(@PathVariable String ISBN) throws NotFoundException {
        Optional<Book> book = bookService.findByISBN(ISBN);
        return book.map(ResponseEntity::ok)
                .orElseThrow(NoSuchElementException::new);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/admin/delete/{ISBN}")
    public ResponseEntity<String> deleteByISBN(@PathVariable String ISBN) throws NotFoundException {
        return new ResponseEntity<>(bookService.deleteBookByISBN(ISBN), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/admin/update/{ISBN}")
    public ResponseEntity<Book> updateByISBN(@PathVariable String ISBN, @Valid @RequestBody BookDto bookDto) throws NotFoundException {
        bookDto.setAdmin(securityService.getCurrentUser());
        Book book = bookService.updateBookByISBN(ISBN, bookDto);
        return ResponseEntity.ok(book);

    }
}
