package com.lab_lib.restapi.Models;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import org.hibernate.annotations.BatchSize;

@Entity
@Table(name = "book")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String title;

    private String description;

    private BigDecimal price;

    @Column(name = "publish_month")
    private Integer publishMonth;

    @Column(name = "publish_year")
    private Integer publishYear;

    @ManyToOne
    @JoinColumn(name = "publisher_id")
    private Publisher publisher;

    @ManyToMany
    @JoinTable(
        name = "book_author",
        joinColumns = @JoinColumn(name = "book_id"),
        inverseJoinColumns = @JoinColumn(name = "author_id")
    )
    @BatchSize(size = 50)
    private Set<Author> authors;

    @ManyToMany
    @JoinTable(
        name = "book_category",
        joinColumns = @JoinColumn(name = "book_id"),
        inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    @BatchSize(size = 50)
    private Set<Category> categories;
    @ManyToMany(mappedBy = "books")

    private Set<PersonalLibrary> personalLibraries = new HashSet<>();
}
