package com.example.library2.controller;

import com.example.library2.dto.LibraryDto;
import com.example.library2.entity.Library;
import com.example.library2.entity.book.Book;
import com.example.library2.exception.NotFoundException;
import com.example.library2.service.LibraryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/library")
public class LibraryController {
    @Autowired
    private LibraryService libraryService;

    @PreAuthorize(value = "hasRole('ADMIN')")
    @PostMapping("/admin/create")
    public ResponseEntity<Library> createBook(@RequestParam String username, @RequestParam String isbn, @RequestBody LibraryDto libraryDto) {
        Library libraryEntry = libraryService.createLibrary(username, isbn, libraryDto);
        return ResponseEntity.ok(libraryEntry);
    }

    @GetMapping("/available-books")
    public ResponseEntity<List<Book>> getAvailableBooks() throws NotFoundException {
        List<Book> availableBooks = libraryService.getAvailableBooks();
        return ResponseEntity.ok(availableBooks);
    }
}
