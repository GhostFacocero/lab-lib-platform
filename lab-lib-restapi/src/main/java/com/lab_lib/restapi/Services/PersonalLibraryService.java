package com.lab_lib.restapi.Services;

import com.lab_lib.restapi.DTO.PersonalLibrary.*;
import com.lab_lib.restapi.DTO.Book.*;
import com.lab_lib.restapi.Repositories.BookRepository;
import com.lab_lib.restapi.Repositories.PersonalLibraryRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import com.lab_lib.restapi.Models.Book;
import com.lab_lib.restapi.Models.PersonalLibrary;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.ArrayList;
import java.util.List;

@Service
public class PersonalLibraryService {
    
    @PersistenceContext
    private EntityManager entityManager;

    private final PersonalLibraryRepository personalLibraryRepository;
    private final BookRepository bookRepository;

    public PersonalLibraryService(PersonalLibraryRepository personalLibraryRepository, 
    BookRepository bookRepository) {
        this.personalLibraryRepository = personalLibraryRepository;
        this.bookRepository = bookRepository;
    }

    @Transactional
    public List<PersonalLibrary> findAllByUserId(Long userId) {
        if(userId == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        return personalLibraryRepository.findAllByUserId(userId);
    }

    @Transactional
    public synchronized PersonalLibrary addLibrary(AddLibraryRequest newLibrary, Long userId) {

        //check per vedere se l'utente esiste
        if(userId == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User is not authenticated");
        }

        String name = newLibrary.getName();

        //check per vedere se esiste già una libreria con lo stesso nome associata allo stesso utente
        if(personalLibraryRepository.existsByNameAndUserId(name, userId)) {
            throw new IllegalArgumentException("Personal library with the same name already exists");
        }

        PersonalLibrary library = new PersonalLibrary();
        library.setUserId(userId);
        library.setName(name);

        PersonalLibrary saved = personalLibraryRepository.save(library);
        return saved;

    }


    @Transactional
    public synchronized void addBookToLibrary(AddBookToLibraryRequest req, Long userId) {

        if(userId == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);    
        }

        Long plId = req.getPlId();
        Long bookId = req.getBookId();

        if(personalLibraryRepository.existsByIdAndBooksId(plId, bookId)) {
            throw new IllegalArgumentException("Selected book is already in this library");
        }

        PersonalLibrary library = personalLibraryRepository.findById(plId)
        .orElseThrow(() -> new RuntimeException("Personal library not found"));

        Book book = bookRepository.findById(bookId)
        .orElseThrow(() -> new RuntimeException("Book not found"));

        library.addBook(book);
        personalLibraryRepository.save(library);

    }

    
    @Transactional
    public List<BookDTO> getLibraryBooks(Long libId, Long userId) {

        if(userId == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User is not authenticated");
        }

        List<BookDTO> books = personalLibraryRepository.findById(libId)
        .orElseThrow(() -> new RuntimeException("Personal library not found"))
        .getBooks().stream().map(b -> new BookDTO(b)).toList();
        return books;
        
    }

    @Transactional
    public List<BookDTO> searchBookByTitle(Long libId, Long userId, String title) {

        if(userId == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

        Set<Book> books = personalLibraryRepository.findById(libId)
        .orElseThrow(() -> new RuntimeException("Personal library not found"))
        .getBooks();

        if(title == null || title.isBlank()) {
            return books.stream().map(b -> new BookDTO(b)).toList();
        }

        String titleLow = title.toLowerCase();
        return books.stream().filter(b -> b.getTitle().toLowerCase().contains(titleLow))
        .map(b -> new BookDTO(b)).toList();
    
    }

    @Transactional
    public List<BookDTO> searchBookByAuthors(Long libId, Long userId, String authors) {

        if(userId == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

        Set<Book> books = personalLibraryRepository.findById(libId)
        .orElseThrow(() -> new RuntimeException("Personal library not found"))
        .getBooks();

        if(authors == null || authors.isBlank()) {
            return books.stream().map(b -> new BookDTO(b)).toList();
        }

        String authorsLow = authors.toLowerCase();
        return books.stream().filter(b -> b.getAuthors()
        .stream().anyMatch(a -> a.getName().toLowerCase().contains(authorsLow)))
        .map(b -> new BookDTO(b)).toList();

    }

    @Transactional
    public List<BookDTO> searchBookBytitleAndAuthors(Long libId, Long userId, String title, String authors) {

        if(userId == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

        Set<Book> books = personalLibraryRepository.findById(libId)
        .orElseThrow(() -> new RuntimeException("Personal library not found"))
        .getBooks();

        if((title == null || title.isBlank()) && (authors == null || authors.isBlank())) {
            return books.stream().map(b -> new BookDTO(b)).toList();
        } else if((title == null || title.isBlank()) && (!authors.isBlank() || authors != null)) {
            return searchBookByAuthors(libId, userId, authors);
        } else if((!title.isBlank() || title != null) && (authors == null || authors.isBlank())) {
            return searchBookByTitle(libId, userId, title);
        }

        String titleLow = title.toLowerCase();
        String authorsLow = authors.toLowerCase();
        List<Book> firstFilter = books.stream().filter(b -> b.getTitle().toLowerCase().contains(titleLow))
        .toList();
        return firstFilter.stream().filter(b -> b.getAuthors()
        .stream().anyMatch(a -> a.getName().toLowerCase().contains(authorsLow)))
        .map(b -> new BookDTO(b)).toList();

    }

}
