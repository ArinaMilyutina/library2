package com.example.library2.service;

import com.example.library2.dto.BookDto;
import com.example.library2.entity.book.Book;
import com.example.library2.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class BookService {
    @Autowired
    private BookRepository bookRepository;

    public Book createBook(BookDto bookDto) {
        try {
            Book book = Book.builder()
                    .title(bookDto.getTitle())
                    .author(bookDto.getAuthor())
                    .description(bookDto.getDescription())
                    .genre(bookDto.getGenre())
                    .ISBN(bookDto.getISBN())
                    .admin(bookDto.getAdmin())
                    .build();
            return bookRepository.save(book);
        } catch (DataIntegrityViolationException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "ISBN already exists");
        }
    }
}
