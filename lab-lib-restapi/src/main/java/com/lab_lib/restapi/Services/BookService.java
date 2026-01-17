package com.lab_lib.restapi.Services;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lab_lib.restapi.DTO.Book.BookDTO;
import com.lab_lib.restapi.DTO.Book.RecommendedBookDTO;
import com.lab_lib.restapi.Exceptions.AuthenticationException;
import com.lab_lib.restapi.Models.RatingName;
import com.lab_lib.restapi.Models.AppUser;
import com.lab_lib.restapi.Models.Book;
import com.lab_lib.restapi.Repositories.BookRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Service
public class BookService {

    @PersistenceContext
    private EntityManager entityManager;

    private final BookRepository bookRepository;
    private final RecommendedBookService recommendedBookService;
    private final UserService userService;


    public BookService(BookRepository bookRepository, RecommendedBookService recommendedBookService, UserService userService) {
        this.bookRepository = bookRepository;
        this.recommendedBookService = recommendedBookService;
        this.userService = userService;
    }

 
    @Transactional
    public Page<BookDTO> getBooks(int page, int size) {

        int maxSize = 100;
        if(size > maxSize) {
            throw new IllegalArgumentException("Page size must not exceed " + maxSize);
        }
        return bookRepository.findAll(PageRequest.of(page, size))
        .map(BookDTO::new);

    }


    @Transactional
    public Page<BookDTO> searchByTitle(String title, int page, int size) {

        int maxSize = 100;
        if(size > maxSize) {
            throw new IllegalArgumentException("Page size must not exceed " + maxSize);
        }
        return bookRepository.findByTitleContaining(title, PageRequest.of(page, size))
        .map(BookDTO::new);

    }


    @Transactional
    public Page<BookDTO> searchByAuthor(String author, int page, int size) {

        int maxSize = 100;
        if(size > maxSize) {
            throw new IllegalArgumentException("Page size must not exceed " + maxSize);
        }
        return bookRepository.findByAuthorsNameContaining(author, PageRequest.of(page, size))
        .map(BookDTO::new);
 

    }


    @Transactional
    public Page<BookDTO> searchByTitleAndAuthor(String title, String author, int page, int size) {

        int maxSize = 100;
        if(size > maxSize) {
            throw new IllegalArgumentException("Page size must not exceed " + maxSize);
        }
        return bookRepository.findByTitleAndAuthorsNameContaining(title, author, PageRequest.of(page, size))
        .map(BookDTO::new);
        
    }


    @Transactional
    public Page<BookDTO> searchByRatingNameAndEvaluation(String name, int evaluation, int page, int size) {

        int maxSize = 100;
        if(size > maxSize) {
            throw new IllegalArgumentException("Page size must not exceed " + maxSize);
        }
        RatingName ratingName = new RatingName();
        ratingName.setName(name);
        return bookRepository.findByRatingsNameAndRatingsEvaluation(ratingName, evaluation, PageRequest.of(page, size))
        .map(BookDTO::new);
        
    }


    @Transactional
    public Page<BookDTO> getBooksByLibId(Long userId, Long libId, int page, int size) {
        if(userId == null) {
            throw new AuthenticationException("Authentication required", "PersonalLibraryService.findAllByUserId");
        }
        int maxSize = 100;
        if(size > maxSize) {
            throw new IllegalArgumentException("Page size must not exceed " + maxSize);
        }
        return bookRepository.findByLibrariesId(libId, PageRequest.of(page, size))
        .map(BookDTO::new);
    }


    @Transactional
    public Page<BookDTO> searchByLibIdAndTitle(Long userId, Long libId, String title, int page, int size) {

        if(userId == null) {
            throw new AuthenticationException("Authentication required", "PersonalLibraryService.findAllByUserId");
        }
        int maxSize = 100;
        if(size > maxSize) {
            throw new IllegalArgumentException("Page size must not exceed " + maxSize);
        }
        return bookRepository.findByLibrariesIdAndTitleContaining(libId, title, PageRequest.of(page, size))
        .map(BookDTO::new);

    }


    @Transactional
    public Page<BookDTO> searchByLibIdAndAuthor(Long userId, Long libId, String author, int page, int size) {

        if(userId == null) {
            throw new AuthenticationException("Authentication required", "PersonalLibraryService.findAllByUserId");
        }
        int maxSize = 100;
        if(size > maxSize) {
            throw new IllegalArgumentException("Page size must not exceed " + maxSize);
        }
        return bookRepository.findByLibrariesIdAndAuthorsNameContaining(libId, author, PageRequest.of(page, size))
        .map(BookDTO::new);

    }


    @Transactional
    public Page<BookDTO> searchByLibIdAndTitleAndAuthor(Long userId, Long libId, String title, String author, int page, int size) {

        if(userId == null) {
            throw new AuthenticationException("Authentication required", "PersonalLibraryService.findAllByUserId");
        }
        int maxSize = 100;
        if(size > maxSize) {
            throw new IllegalArgumentException("Page size must not exceed " + maxSize);
        }
        return bookRepository.findByLibrariesIdAndTitleContainingIgnoreCaseAndAuthorsNameContainingIgnoreCase(libId, title, author, PageRequest.of(page, size))
        .map(BookDTO::new);

    }


    @Transactional
    public List<RecommendedBookDTO> getRecommendedBooks(Long bookId) {

        Book book = bookRepository.findById(bookId)
        .orElseThrow(() -> new NoSuchElementException("Book not found"));
        return recommendedBookService.getRecommendedBooks(book);

    }


    @Transactional
    public RecommendedBookDTO addRecommendedBook(Long bookId, Long recommendedBookId, Long userId) {

        if(userId == null) {
            throw new AuthenticationException("Authentication required", "BookService.addRecommendedBook");
        }
        Book book = bookRepository.findById(bookId)
        .orElseThrow(() -> new NoSuchElementException("Book not found"));
        Book recommendedBook = bookRepository.findById(recommendedBookId)
        .orElseThrow(() -> new NoSuchElementException("Recommended book not found"));
        AppUser user = userService.findUserById(userId);
        return recommendedBookService.addRecommendedBook(book, recommendedBook, user);

    }


    @Transactional
    public void removeRecommendedBook(Long bookId, Long recommendedBookId, Long userId) {

        if(userId == null) {
            throw new AuthenticationException("Authentication required", "BookService.addRecommendedBook");
        }
        Book book = bookRepository.findById(bookId)
        .orElseThrow(() -> new NoSuchElementException("Book not found"));
        Book recommendedBook = bookRepository.findById(recommendedBookId)
        .orElseThrow(() -> new NoSuchElementException("Recommended book not found"));
        AppUser user = userService.findUserById(userId);
        recommendedBookService.removeRecommendedBook(book, recommendedBook, user);

    }


    @Transactional
    public Book findBookById(Long id) {
        return bookRepository.findById(id)
        .orElseThrow(() -> new NoSuchElementException("Book not found"));
    }

}