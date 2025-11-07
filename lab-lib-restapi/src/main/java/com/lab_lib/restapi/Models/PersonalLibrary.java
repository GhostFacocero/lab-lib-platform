package com.lab_lib.restapi.Models;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
    name = "personal_library",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"ID"})
    }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PersonalLibrary {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(name = "name", nullable = false)
    private String name;

    @ManyToMany
    @JoinTable(
        name = "book_pl",
        joinColumns = @JoinColumn(name = "id_pl"),
        inverseJoinColumns = @JoinColumn(name = "id_book")
    )

    @Builder.Default

    private Set<Book> books = new HashSet<>();

    public void addBook(Book book) {
        books.add(book);
        book.getPersonalLibraries().add(this);
    }

    public void removeBook(Book book) {
        books.remove(book);
        book.getPersonalLibraries().remove(this);
    }

    public boolean hasBook(Book book) {
        return books.contains(book);
    }

}