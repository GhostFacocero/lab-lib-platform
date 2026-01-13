package com.lab_lib.restapi.Services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.lab_lib.restapi.DTO.Book.BookDTO;
import com.lab_lib.restapi.Models.RatingName;
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
    public Page<BookDTO> getBooks(int page, int size) {

        int maxSize = 100;

        if (size > maxSize) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST, "Page size must not exceed " + maxSize
            );
        }
        
        return bookRepository.findAll(PageRequest.of(page, size))
        .map(BookDTO::new);

    }


    @Transactional
    public Page<BookDTO> searchByTitle(String title, int page, int size) {

        int maxSize = 100;

        if (size > maxSize) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST, "Page size must not exceed " + maxSize
            );
        }

        return bookRepository.findByTitleContaining(title, PageRequest.of(page, size))
        .map(BookDTO::new);

    }


    @Transactional
    public Page<BookDTO> searchByAuthor(String author, int page, int size) {

        int maxSize = 100;

        if (size > maxSize) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST, "Page size must not exceed " + maxSize
            );
        }

        return bookRepository.findByAuthorsNameContaining(author, PageRequest.of(page, size))
        .map(BookDTO::new);
 

    }


    @Transactional
    public Page<BookDTO> searchByTitleAndAuthor(String title, String author, int page, int size) {

        int maxSize = 100;

        if (size > maxSize) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST, "Page size must not exceed " + maxSize
            );
        }

        return bookRepository.findByTitleAndAuthorsNameContaining(title, author, PageRequest.of(page, size))
        .map(BookDTO::new);
        
    }


    @Transactional
    public Page<BookDTO> searchByRatingNameAndEvaluation(String name, int evaluation, int page, int size) {

        int maxSize = 100;
        
        if(size > maxSize) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST, "Page size must not exceed " + maxSize
            );
        }

        RatingName ratingName = new RatingName();
        ratingName.setName(name);
        return bookRepository.findByRatingsNameAndRatingsEvaluation(ratingName, evaluation, PageRequest.of(page, size))
        .map(BookDTO::new);
        
    }


    @Transactional
    public Page<BookDTO> searchByLibIdAndTitle(Long userId, Long libId, String title, int page, int size) {

        if(userId == null) {
            throw new ResponseStatusException(
                HttpStatus.UNAUTHORIZED,
                "Authentication required"
            );
        }

        int maxSize = 100;

        if (size > maxSize) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST, "Page size must not exceed " + maxSize
            );
        }

        return bookRepository.findByLibrariesIdAndTitleContaining(libId, title, PageRequest.of(page, size))
        .map(BookDTO::new);

    }


    @Transactional
    public Page<BookDTO> searchByLibIdAndAuthor(Long userId, Long libId, String author, int page, int size) {

        if(userId == null) {
            throw new ResponseStatusException(
                HttpStatus.UNAUTHORIZED,
                "Authentication required"
            );
        }

        int maxSize = 100;

        if (size > maxSize) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST, "Page size must not exceed " + maxSize
            );
        }

        return bookRepository.findByLibrariesIdAndAuthorsNameContaining(libId, author, PageRequest.of(page, size))
        .map(BookDTO::new);

    }


    @Transactional
    public Page<BookDTO> searchByLibIdAndTitleAndAuthor(Long userId, Long libId, String title, String author, int page, int size) {

        if(userId == null) {
            throw new ResponseStatusException(
                HttpStatus.UNAUTHORIZED,
                "Authentication required"
            );
        }
        
        int maxSize = 100;

        if (size > maxSize) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST, "Page size must not exceed " + maxSize
            );
        }

        return bookRepository.findByLibrariesIdAndTitleContainingIgnoreCaseAndAuthorsNameContainingIgnoreCase(libId, title, author, PageRequest.of(page, size))
        .map(BookDTO::new);

    }

}