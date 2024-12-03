package com.example.library2.service;

import com.example.library2.dto.BookDto;
import com.example.library2.entity.book.Book;
import com.example.library2.exception.AlreadyExistsException;
import com.example.library2.exception.NotFoundException;
import com.example.library2.mapper.BookMapper;
import com.example.library2.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;

import java.util.Optional;

@Service
public class BookService {
    @Autowired
    private BookRepository bookRepository;
    private final static String BOOK_ALREADY_EXISTS = "The book with this isbn already exists!!!";
    private final static String BOOKS_NOT_FOUND = "Book not found.";
    private final static String BOOK_BY_ID = "A book with this id not found.";

    private final static String BOOK_BY_ISBN = "A book with isbn not found.";
    private final static String DELETE_BOOK_BY_ID = "The book has been deleted.";


    public Book createBook(BookDto bookDto) {
        Book book = BookMapper.INSTANCE.bookDtoToBook(bookDto);
        Optional<Book> existingBook = bookRepository.findByISBN(book.getISBN());
        if (existingBook.isPresent()) {
            throw new AlreadyExistsException(BOOK_ALREADY_EXISTS);
        }
        return bookRepository.save(book);
    }

    public List<Book> findAll() throws NotFoundException {
        List<Book> bookList = bookRepository.findAll();
        if (bookList.isEmpty()) {
            throw new NotFoundException(BOOKS_NOT_FOUND);
        }
        return bookList;
    }

    public Optional<Book> findById(Long id) throws NotFoundException {
        Optional<Book> book = bookRepository.findById(id);
        if (book.isEmpty()) {
            throw new NotFoundException(BOOK_BY_ID);
        }
        return book;
    }

    public Optional<Book> findByISBN(String ISBN) throws NotFoundException {
        Optional<Book> book = bookRepository.findByISBN(ISBN);
        if (book.isEmpty()) {
            throw new NotFoundException(BOOK_BY_ISBN);
        }
        return book;
    }

    @Transactional
    public String deleteBookByISBN(String ISBN) throws NotFoundException {
        Optional<Book> book = bookRepository.findByISBN(ISBN);
        if (book.isEmpty()) {
            throw new NotFoundException(BOOKS_NOT_FOUND);
        }
        bookRepository.deleteByISBN(ISBN);
        return DELETE_BOOK_BY_ID;
    }

    public Book updateBookByISBN(String ISBN, BookDto bookDto) throws NotFoundException {
        try {
            Optional<Book> byISBN = findByISBN(ISBN);
            Book book = BookMapper.INSTANCE.bookDtoToBook(bookDto);
            book.setId(byISBN.get().getId());
            return bookRepository.save(book);
        } catch (DataIntegrityViolationException e) {
            throw new AlreadyExistsException(BOOK_ALREADY_EXISTS);
        }

    }
}
