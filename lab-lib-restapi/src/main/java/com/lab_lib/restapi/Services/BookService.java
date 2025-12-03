package com.lab_lib.restapi.Services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import com.lab_lib.restapi.Models.Book;
import com.lab_lib.restapi.Repositories.BookRepository;

public class BookService {

    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }
 
    public Page<Book> getBooks(int page, int size) {
        int maxSize = 100;
        if (size > maxSize) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST, "Page size must not exceed " + maxSize
            );
        }
        
        return bookRepository.findAll(PageRequest.of(page, size));
    }

}