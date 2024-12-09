package com.example.library2.service;

import com.example.library2.dto.LibraryDto;
import com.example.library2.entity.Library;
import com.example.library2.entity.book.Book;
import com.example.library2.entity.user.User;
import com.example.library2.exception.NotFoundException;
import com.example.library2.repository.LibraryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LibraryServiceTest {
    @InjectMocks
    private LibraryService libraryService;

    @Mock
    private LibraryRepository libraryRepository;

    @Mock
    private BookService bookService;

    @Mock
    private UserService userService;


    private static final Book book = new Book();
    private static final LibraryDto libraryDto = new LibraryDto();
    private static final List<Book> allBooks = List.of(book);
    private static final User user = new User();
    private static final String ISBN = "1234567890123";
    private static final String USERNAME = "Arina20";

    @Test
    void createLibraryTest_ReturnLibrary() throws NotFoundException {
        when(userService.findByUsername(USERNAME)).thenReturn(Optional.of(user));
        when(bookService.findByISBN(ISBN)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> {
            libraryService.createLibrary(USERNAME, ISBN, libraryDto);
        });
        verify(libraryRepository, never()).save(any());
    }

    @Test
    public void createLibrary_UserNotFound_ThrowsNotFoundException() {
        when(userService.findByUsername(USERNAME)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> {
            libraryService.createLibrary(USERNAME, ISBN, libraryDto);
        });
        verify(libraryRepository, never()).save(any());
    }

    @Test
    public void createLibrary_BookNotFound_ThrowsNotFoundException() throws NotFoundException {
        when(userService.findByUsername(USERNAME)).thenReturn(Optional.of(user));
        when(bookService.findByISBN(ISBN)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> {
            libraryService.createLibrary(USERNAME, ISBN, libraryDto);
        });
        verify(libraryRepository, never()).save(any());
    }

    @Test
    public void getAvailableBooks_ReturnsAvailableBooks() throws NotFoundException {
        Library libraryEntry = new Library();
        libraryEntry.setBook(book);
        libraryEntry.setReturnDate(null);
        when(libraryRepository.findByReturnDateIsNull()).thenReturn(List.of(libraryEntry));
        when(bookService.findAll()).thenReturn(allBooks);
        List<Book> availableBooks = libraryService.getAvailableBooks();
        assertTrue(availableBooks.isEmpty());
        verify(bookService).findAll();
    }

    @Test
    public void getAvailableBooks_WhenNoBooksBorrowed_ReturnsAllBooks() throws NotFoundException {
        when(libraryRepository.findByReturnDateIsNull()).thenReturn(Collections.emptyList());
        when(bookService.findAll()).thenReturn(allBooks);
        List<Book> availableBooks = libraryService.getAvailableBooks();
        assertEquals(allBooks.size(), availableBooks.size());
        assertEquals(allBooks, availableBooks);
        verify(bookService).findAll();
    }
}

