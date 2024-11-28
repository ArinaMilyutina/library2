package com.example.library2.entity.book;


import lombok.*;

import javax.persistence.*;
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
    @ElementCollection(targetClass = Genre.class)
    @CollectionTable(name = "entity_genre", joinColumns = @JoinColumn(name = "entity_id"))
    @Enumerated(EnumType.STRING)
    private Set<Genre> genre;

}
