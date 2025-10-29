package com.lab_lib.restapi.Services;

import com.lab_lib.restapi.DTO.PersonalLibrary.*;
import com.lab_lib.restapi.Repositories.BookRepository;
import com.lab_lib.restapi.Repositories.PersonalLibraryRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

import com.lab_lib.restapi.Models.Book;
import com.lab_lib.restapi.Models.PersonalLibrary;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

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
        return personalLibraryRepository.findAllByUserId(userId);
    }

    @Transactional
    public synchronized PersonalLibrary addLibrary(AddLibraryRequest newLibrary) {

        String name = newLibrary.getName();
        Long userId = newLibrary.getUserId();

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

    public synchronized void addBookToLibrary(AddBookToLibraryRequest req) {
        Long plId = req.getPlId();
        Long bookId = req.getBookId();

        if(personalLibraryRepository.existsByIdPlAndIdBook(plId, bookId)) {
            throw new IllegalArgumentException("Selected book is already in this library");
        }

        PersonalLibrary library = personalLibraryRepository.findById(plId)
        .orElseThrow(() -> new RuntimeException("Personal library not found"));

        Book book = bookRepository.findById(bookId)
        .orElseThrow(() -> new RuntimeException("Book not found"));

        library.getBooks().add(book);
        personalLibraryRepository.save(library);

    }

    public List<Book> getLibraryBooks(GetLibraryBooksRequest libId) {
        Long id = libId.getLibId();
        return personalLibraryRepository.findBooksById(id);
    }

}
