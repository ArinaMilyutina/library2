package com.example.library2.service;

import com.example.library2.dto.BookDto;
import com.example.library2.entity.book.Book;
import com.example.library2.exception.NotFoundException;
import com.example.library2.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

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

    public List<Book> findAll() throws NotFoundException {
        List<Book> bookList = bookRepository.findAll();
        if (bookList.isEmpty()) {
            throw new NotFoundException("Books not found");
        }
        return bookList;
    }

    public Optional<Book> findById(Long id) {
        return bookRepository.findById(id);
    }

    public Optional<Book> findByISBN(String ISBN) {
        return bookRepository.findByISBN(ISBN);
    }

    @Transactional
    public String deleteBookByISBN(String ISBN) {
        bookRepository.deleteByISBN(ISBN);
        return "The book has been deleted";
    }

    public Book updateBookByISBN(String ISBN, BookDto bookDto) {
        Optional<Book> byISBN = findByISBN(ISBN);
        Book book = Book.builder()
                .title(bookDto.getTitle())
                .author(bookDto.getAuthor())
                .description(bookDto.getDescription())
                .ISBN(bookDto.getDescription())
                .genre(bookDto.getGenre())
                .admin(bookDto.getAdmin())
                .build();
        return bookRepository.save(book);
    }
}
