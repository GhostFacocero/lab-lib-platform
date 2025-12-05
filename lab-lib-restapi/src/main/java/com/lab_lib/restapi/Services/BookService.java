package com.lab_lib.restapi.Services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.lab_lib.restapi.Models.Book;
import com.lab_lib.restapi.Repositories.BookRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Service
public class BookService {

    @PersistenceContext
    private EntityManager entityManager;

    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }
 
    @Transactional
    public Page<Book> getBooks(int page, int size) {

        int maxSize = 100;

        if (size > maxSize) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST, "Page size must not exceed " + maxSize
            );
        }
        
        return bookRepository.findAll(PageRequest.of(page, size));

    }

    @Transactional
    public Page<Book> searchByTitle(String title, int page, int size) {
        return bookRepository.findByTitleContaining(title, PageRequest.of(page, size));
    }

    @Transactional
    public Page<Book> searchByAuthor(String author, int page, int size) {
        return bookRepository.findByAuthorsNameContaining(author, PageRequest.of(page, size));
    }

    @Transactional
    public Page<Book> searchByTitleAndAuthor(String title, String author, int page, int size) {
        return bookRepository.findByTitleAndAuthorsNameContaining(title, author, PageRequest.of(page, size));
    }

}