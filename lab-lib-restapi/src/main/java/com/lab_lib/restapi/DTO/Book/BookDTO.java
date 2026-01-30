// Emanuele Contini, matricola 756441
// Emanuele Gobessi, matricola 757599
// Diego Guidi, matricola 758420
// Nicola Curchi, matricola 757786
// Mirko Gurzau, matricola 757925

package com.lab_lib.restapi.DTO.Book;

import java.math.BigDecimal;
import java.util.List;

import com.lab_lib.restapi.Models.Book;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data

public class BookDTO {

    @NotNull
    private Long id;

    @NotBlank
    private String title;
    private String description;

    @DecimalMin("0.0")
    private BigDecimal price;
    private Integer publishMonth;
    private Integer publishYear;
    private String publisher;

    @NotEmpty
    private List<String> authors;

    @NotEmpty
    private List<String> categories;

    public BookDTO(Book book) {
        this.id = book.getId();
        this.title = book.getTitle();
        this.description = book.getDescription();
        this.price = book.getPrice();
        this.publishMonth = book.getPublishMonth();
        this.publishYear = book.getPublishYear();
        this.publisher = book.getPublisher().getName();
        this.authors = book.getAuthors().stream().map(a -> a.getName()).toList();
        this.categories = book.getCategories().stream().map(c -> c.getName()).toList();
    }
    
}