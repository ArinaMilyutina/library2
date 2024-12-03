package com.example.library2.dto;

import com.example.library2.entity.book.Genre;
import com.example.library2.entity.user.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.*;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookDto {
    @NotBlank(message = "Title cannot be blank")
    @Size(min = 1, max = 255, message = "Title must be between 1 and 255 characters")
    private String title;
    @NotBlank(message = "Author cannot be blank")
    @Size(min =1, max = 255, message = "Author must be between 1 and 255 characters")
    private String author;
    @Pattern(regexp = "^\\d{13}$", message = "ISBN must consist of 13 digits!!!")
    private String ISBN;
    @NotBlank(message = "Description cannot be blank")
    @Size(min = 1, max = 255, message = "Description must be between 1 and 255 characters")
    private String description;
    @NotNull(message = "Genre cannot be null")
    @NotEmpty(message = "Genre cannot be empty")
    @Enumerated(EnumType.STRING)
    private Set<Genre> genre;
    private User admin;
}
