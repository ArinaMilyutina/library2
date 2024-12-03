package com.example.library2.service;

import com.example.library2.dto.LibraryDto;
import com.example.library2.entity.Library;
import com.example.library2.entity.book.Book;
import com.example.library2.entity.user.User;
import com.example.library2.exception.NotFoundException;
import com.example.library2.mapper.LibraryMapper;
import com.example.library2.repository.LibraryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LibraryService {
    @Autowired
    private LibraryRepository libraryRepository;
    @Autowired
    private BookService bookService;
    @Autowired
    private UserService userService;

    public Library createLibrary(String username, String isbn, LibraryDto libraryDto) {
        User user = userService.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User  not found"));
        Book book = bookService.findByISBN(isbn)
                .orElseThrow(() -> new RuntimeException("Book not found"));

        Library libraryEntry = LibraryMapper.INSTANCE.LibraryDtoToLibrary(libraryDto);
        libraryEntry.setUser(user);
        libraryEntry.setBook(book);
        libraryEntry.setBorrowDate(LocalDateTime.now());
        libraryEntry.setReturnDate(libraryEntry.getReturnDate());
        return libraryRepository.save(libraryEntry);
    }

    public List<Book> getAvailableBooks() throws NotFoundException {
        List<Long> borrowedBookIds = libraryRepository.findByReturnDateIsNull()
                .stream()
                .map(library -> library.getBook().getId()).toList();
        List<Book> allBooks = bookService.findAll();
        return allBooks.stream()
                .filter(book -> !borrowedBookIds.contains(book.getId()))
                .collect(Collectors.toList());
    }

}
