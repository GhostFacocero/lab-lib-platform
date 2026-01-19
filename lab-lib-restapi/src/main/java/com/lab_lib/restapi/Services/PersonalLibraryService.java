package com.lab_lib.restapi.Services;

import com.lab_lib.restapi.DTO.PersonalLibrary.*;
import com.lab_lib.restapi.Exceptions.AuthenticationException;
import com.lab_lib.restapi.DTO.Book.*;
import com.lab_lib.restapi.Repositories.PersonalLibraryRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import com.lab_lib.restapi.Models.AppUser;
import com.lab_lib.restapi.Models.Book;
import com.lab_lib.restapi.Models.PersonalLibrary;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class PersonalLibraryService {
    
    @PersistenceContext
    private EntityManager entityManager;

    private final PersonalLibraryRepository personalLibraryRepository;
    private final BookService bookService;


    public PersonalLibraryService(PersonalLibraryRepository personalLibraryRepository, BookService bookService) {
        this.personalLibraryRepository = personalLibraryRepository;
        this.bookService = bookService;
    }


    @Transactional
    public List<PersonalLibraryDTO> findAllByUserId(Long userId) {

        if(userId == null) {
            throw new AuthenticationException("Authentication required", "PersonalLibraryService.findAllByUserId");
        }
        List<PersonalLibrary> personalLibraries = personalLibraryRepository.findAllByUserId(userId);
        return personalLibraries.stream().map(p -> p.toDTO()).toList();
        
    }


    @Transactional
    public synchronized PersonalLibraryDTO addLibrary(AddLibraryRequest newLibrary, Long userId) {

        //check per vedere se l'utente esiste
        if(userId == null) {
            throw new AuthenticationException("Authentication required", "PersonalLibraryService.findAllByUserId");
        }
        String name = newLibrary.getName();

        //check per vedere se esiste già una libreria con lo stesso nome associata allo stesso utente
        if(personalLibraryRepository.existsByNameAndUserId(name, userId)) {
            throw new IllegalStateException("Personal library with the same name already exists");
        }
        PersonalLibrary library = new PersonalLibrary();
        AppUser appUser = new AppUser();
        appUser.setId(userId);
        library.setUser(appUser);
        library.setName(name);
        PersonalLibrary saved = personalLibraryRepository.save(library);
        return saved.toDTO();

    }


    @Transactional
    public synchronized void addBookToLibrary(AddBookToLibraryRequest req, Long userId) {

        if(userId == null) {
            throw new AuthenticationException("Authentication required", "PersonalLibraryService.findAllByUserId");
        }
        Long plId = req.getPlId();
        Long bookId = req.getBookId();
        if(personalLibraryRepository.existsByIdAndBooksId(plId, bookId)) {
            throw new IllegalStateException("Selected book is already in this library");
        }
        PersonalLibrary library = personalLibraryRepository.findById(plId)
        .orElseThrow(() -> new NoSuchElementException("Personal library not found"));
        Book book = bookService.findBookById(bookId);
        library.addBook(book);
        personalLibraryRepository.save(library);

    }

    
    @Transactional
    public List<BookDTO> getLibraryBooks(Long libId, Long userId) {

        if(userId == null) {
            throw new AuthenticationException("Authentication required", "PersonalLibraryService.findAllByUserId");
        }
        List<BookDTO> books = personalLibraryRepository.findById(libId)
        .orElseThrow(() -> new NoSuchElementException("Personal library not found"))
        .getBooks().stream().map(b -> new BookDTO(b)).toList();
        return books;
        
    }


    @Transactional
    public void deletePersonalLibrary(Long libId, Long userId) {

        if(userId == null) {
            throw new AuthenticationException("Authentication required", "PersonalLibraryService.findAllByUserId");
        }
        personalLibraryRepository.deleteById(libId);

    }


    @Transactional
    public void removeBookFromLibrary(Long libId, Long bookId, Long userId) {

        if(userId == null) {
            throw new AuthenticationException("Authentication required", "PersonalLibraryService.findAllByUserId");
        }
        PersonalLibrary personalLibrary = personalLibraryRepository.findById(libId)
        .orElseThrow(() -> new NoSuchElementException("Personal library not found"));
        Book book = bookService.findBookById(bookId);
        if(!personalLibrary.hasBook(book)) {
            throw new IllegalArgumentException("Selected book is not in this library");
        }
        personalLibrary.removeBook(book);

    }

}
