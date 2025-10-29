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
@Data
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

    private Set<Book> books = new HashSet<>();

}