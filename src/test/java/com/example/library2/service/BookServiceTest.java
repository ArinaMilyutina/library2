package com.example.library2.service;

import com.example.library2.dto.BookDto;
import com.example.library2.entity.book.Book;
import com.example.library2.entity.book.Genre;
import com.example.library2.exception.AlreadyExistsException;
import com.example.library2.exception.NotFoundException;
import com.example.library2.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {
    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookService bookService;
    private static final Book book = new Book();
    private static final Long ID = 1L;
    private static final String TITLE = "The Little Prince";
    private static final String AUTHOR = "Antoine de Saint-Exupery";
    private static final String ISBN = "1234567890123";
    private static final String DESCRIPTION = "The fairy tale tells about a Little Prince who visit various planets in space, including Earth";
    private final static String BOOK_ALREADY_EXISTS = "The book with this isbn already exists!!!";
    private final static String BOOKS_NOT_FOUND = "Book not found.";
    private final static String BOOK_BY_ID = "A book with this id not found.";

    private final static String BOOK_BY_ISBN = "A book with isbn not found.";
    private final static String DELETE_BOOK_BY_ISBN = "The book has been deleted.";

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this); // Initialize mocks before each test
    }


    @Test
    void createBookTest_Success() {
        BookDto bookDto = createBookDto();
        Book book = createBook(bookDto);
        when(bookRepository.findByISBN(book.getISBN())).thenReturn(Optional.empty());
        when(bookRepository.save(book)).thenReturn(book);
        Book createBook = bookService.createBook(bookDto);
        assertNotNull(createBook);
        assertEquals(book.getISBN(), createBook.getISBN());
        assertEquals(book.getTitle(), createBook.getTitle());
        verify(bookRepository).findByISBN(book.getISBN());
        verify(bookRepository).save(book);
    }

    @Test
    void createBookTest_AlreadyExists() {
        BookDto bookDto = new BookDto();
        bookDto.setISBN(ISBN);
        book.setISBN(bookDto.getISBN());
        when(bookRepository.findByISBN(book.getISBN())).thenReturn(Optional.of(book));
        Exception exception = assertThrows(AlreadyExistsException.class, () -> {
            bookService.createBook(bookDto);
        });
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(BOOK_ALREADY_EXISTS));
        verify(bookRepository).findByISBN(book.getISBN());
        verify(bookRepository, never()).save(any());
    }

    @Test
    void findAllTest_ReturnListBook() throws NotFoundException {
        List<Book> bookList = new ArrayList<>();
        bookList.add(book);
        when(bookRepository.findAll()).thenReturn(bookList);
        List<Book> result = bookService.findAll();
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals(bookList.get(0), result.get(0));
    }

    @Test
    void findAllTest_ReturnNotFoundException() {
        when(bookRepository.findAll()).thenReturn(Collections.emptyList());
        Exception exception = assertThrows(NotFoundException.class, () -> {
            bookService.findAll();
        });
        assertEquals(BOOKS_NOT_FOUND, exception.getMessage());
    }

    @Test
    void findByIdTest_ReturnBook() throws NotFoundException {
        when(bookRepository.findById(ID)).thenReturn(Optional.of(book));
        Optional<Book> bookOptional = bookService.findById(ID);
        assertTrue(bookOptional.isPresent());
        assertEquals(book, bookOptional.get());
    }

    @Test
    void findByIdTest_NotFoundException() {
        when(bookRepository.findById(ID)).thenReturn(Optional.empty());
        Exception exception = assertThrows(NotFoundException.class, () -> {
            bookService.findById(ID);
        });

        assertEquals(BOOK_BY_ID, exception.getMessage());
    }

    @Test
    void findByISBNTest_ReturnBook() throws NotFoundException {
        when(bookRepository.findByISBN(ISBN)).thenReturn(Optional.of(book));
        Optional<Book> bookOptional = bookService.findByISBN(ISBN);
        assertTrue(bookOptional.isPresent());
        assertEquals(book, bookOptional.get());
    }

    @Test
    void findByISBNTest_NotFoundException() {
        when(bookRepository.findByISBN(ISBN)).thenReturn(Optional.empty());
        Exception exception = assertThrows(NotFoundException.class, () -> {
            bookService.findByISBN(ISBN);
        });

        assertEquals(BOOK_BY_ISBN, exception.getMessage());
    }

    @Test
    void deleteByISBNTest_ReturnMsg() throws NotFoundException {
        when(bookRepository.findByISBN(ISBN)).thenReturn(Optional.of(book));
        String message = bookService.deleteBookByISBN(ISBN);
        verify(bookRepository).deleteByISBN(ISBN);
        assertEquals(DELETE_BOOK_BY_ISBN, message);
    }

    @Test
    void deleteByISBNTest_NotFoundException() {
        when(bookRepository.findByISBN(ISBN)).thenReturn(Optional.empty());
        Exception exception = assertThrows(NotFoundException.class, () -> {
            bookService.deleteBookByISBN(ISBN);
        });

        assertEquals(BOOKS_NOT_FOUND, exception.getMessage());
    }

    @Test
    void updateByISBNTest_ReturnBook() throws NotFoundException {
        when(bookRepository.findByISBN(ISBN)).thenReturn(Optional.of(book));
        BookDto bookDto = createBookDto();
        Book updatedBook = createBook(bookDto);
        when(bookRepository.save(updatedBook)).thenReturn(updatedBook);
        Book result = bookService.updateBookByISBN(ISBN, bookDto);
        assertEquals(updatedBook, result);
        verify(bookRepository).save(updatedBook);
    }

    @Test
    void updateByISBNTest_NotFoundException() {
        BookDto bookDto = createBookDto();
        when(bookRepository.findByISBN(ISBN)).thenReturn(Optional.empty());
        Exception exception = assertThrows(NotFoundException.class, () -> {
            bookService.updateBookByISBN(ISBN, bookDto);
        });
        assertEquals(BOOK_BY_ISBN, exception.getMessage());
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

    private Book createBook(BookDto bookDto) {
        Book book = new Book();
        book.setTitle(bookDto.getTitle());
        book.setAuthor(bookDto.getAuthor());
        book.setISBN(bookDto.getISBN());
        book.setDescription(bookDto.getDescription());
        book.setGenre(bookDto.getGenre());
        return book;
    }

}
