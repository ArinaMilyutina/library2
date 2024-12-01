package com.example.library2.entity.book;

import com.example.library2.entity.user.User;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import java.util.Set;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "books")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String title;
    private String author;
    private String ISBN;
    private String description;
    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(
            name = "book_genres",
            joinColumns = @JoinColumn(name = "book_id")
    )
    @Enumerated(EnumType.STRING)
    private Set<Genre> genre;
    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "admin_id")
    private User admin;
}
