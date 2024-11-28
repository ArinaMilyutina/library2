package com.example.library2.entity;

import jakarta.persistence.*;

import lombok.*;

import java.util.Set;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "books")
public class Book extends AbstractEntity {
    private String title;
    private String author;
    private String ISBN;
    private String description;
    @ElementCollection(targetClass = Genre.class)
    @CollectionTable(name = "entity_genre", joinColumns = @JoinColumn(name = "entity_id"))
    @Enumerated(EnumType.STRING)
    private Set<Genre> genre;

}
