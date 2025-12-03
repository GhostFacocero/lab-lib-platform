package com.lab_lib.restapi.Controllers;

import com.lab_lib.restapi.Models.Book;
import com.lab_lib.restapi.Services.BookService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/books")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping
    public Page<Book> getBooks(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "50") int size) {
        return bookService.getBooks(page, size);
    }
}
