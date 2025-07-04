package com.lab_lib.restapi.Controllers;

import com.lab_lib.restapi.Models.Book;
import com.lab_lib.restapi.Repositories.BookRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/books")
public class BookController {

    private final BookRepository bookRepository;

    public BookController(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @GetMapping
    public Page<Book> getBooks(@RequestParam(defaultValue = "0") int page,
                            @RequestParam(defaultValue = "50") int size) {
        int maxSize = 100;
        if (size > maxSize) {
            size = maxSize;
        }
        return bookRepository.findAll(PageRequest.of(page, size));
    }


}
