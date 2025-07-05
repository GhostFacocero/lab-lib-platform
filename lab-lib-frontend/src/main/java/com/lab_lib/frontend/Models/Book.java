package com.lab_lib.frontend.Models;

import java.math.BigDecimal;
import java.util.Set;

public class Book {
    private Integer id;
    private String title;
    private String description;
    private BigDecimal price;
    private Integer publishMonth;
    private Integer publishYear;
    private Publisher publisher;
    private Set<Author> authors;
    private Set<Category> categories;

    public Book(
        Integer id, 
        String title, 
        String description, 
        BigDecimal price, 
        Integer publishMonth, 
        Integer publishYear, 
        Publisher publisher, 
        Set<Author> authors, 
        Set<Category> categories
    ) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.price = price;
        this.publishMonth = publishMonth;
        this.publishYear = publishYear;
        this.publisher = publisher;
        this.authors = authors;
        this.categories = categories;
    }

    public Book() {
        // Default constructor for deserialization
    }

    public Integer getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public Integer getPublishMonth() {
        return publishMonth;
    }

    public Integer getPublishYear() {
        return publishYear;
    }

    public Publisher getPublisher() {
        return publisher;
    }

    public Set<Author> getAuthors() {
        return authors;
    }

    public Set<Category> getCategories() {
        return categories;
    }
}
