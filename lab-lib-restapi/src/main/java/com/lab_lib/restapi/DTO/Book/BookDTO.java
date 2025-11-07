package com.lab_lib.restapi.DTO.Book;

import java.math.BigDecimal;
import java.util.List;

import com.lab_lib.restapi.Models.Book;


import lombok.Data;
import lombok.NonNull;

@Data

public class BookDTO {

    @NonNull
    private Integer id;

    private String title;
    private String description;
    private BigDecimal price;
    private Integer publishMonth;
    private Integer publishYear;
    private String publisher;
    private List<String> authors;
    private List<String> categoryNames;

    public BookDTO(Book book) {
        this.id = book.getId();
        this.title = book.getTitle();
        this.description = book.getDescription();
        this.price = book.getPrice();
        this.publishMonth = book.getPublishMonth();
        this.publishYear = book.getPublishYear();
        this.publisher = book.getPublisher().getName();
        this.authors = book.getAuthors().toString();
    }
    
}