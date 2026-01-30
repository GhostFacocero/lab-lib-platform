// Emanuele Contini, matricola 756441
// Emanuele Gobessi, matricola 757599
// Diego Guidi, matricola 758420
// Nicola Curchi, matricola 757786
// Mirko Gurzau, matricola 757925

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

/**
 * Servizio per la gestione delle librerie personali degli utenti.
 *
 * <p>Espone operazioni per creare librerie personali, aggiungere/rimuovere
 * libri da una libreria e recuperare gli elementi di una libreria. I metodi
 * che richiedono autenticazione controllano che l'ID dell'utente sia presente
 * e lanciano {@link com.lab_lib.restapi.Exceptions.AuthenticationException}
 * in caso contrario.
 */
@Service
public class PersonalLibraryService {
    
    /** Entity manager per operazioni JPA. */
    @PersistenceContext
    private EntityManager entityManager;

    /** Repository delle librerie personali. */
    private final PersonalLibraryRepository personalLibraryRepository;
    /** Servizio per la gestione dei libri. */
    private final BookService bookService;


    /**
     * Costruttore del servizio.
     *
     * @param personalLibraryRepository repository delle librerie personali
     * @param bookService servizio libri
     */
    public PersonalLibraryService(PersonalLibraryRepository personalLibraryRepository, BookService bookService) {
        this.personalLibraryRepository = personalLibraryRepository;
        this.bookService = bookService;
    }


    /**
     * Recupera tutte le librerie personali dell'utente specificato.
     *
     * @param userId identificatore dell'utente autenticato
     * @return lista di {@link PersonalLibraryDTO}
     * @throws AuthenticationException se {@code userId} è nullo
     */
    @Transactional
    public List<PersonalLibraryDTO> findAllByUserId(Long userId) {

        if(userId == null) {
            throw new AuthenticationException("Authentication required", "PersonalLibraryService.findAllByUserId");
        }
        List<PersonalLibrary> personalLibraries = personalLibraryRepository.findAllByUserId(userId);
        return personalLibraries.stream().map(p -> p.toDTO()).toList();
        
    }


    /**
     * Crea una nuova libreria personale per l'utente autenticato.
     *
     * @param newLibrary dati della libreria da creare
     * @param userId id dell'utente autenticato
     * @return {@link PersonalLibraryDTO} della libreria creata
     * @throws AuthenticationException se {@code userId} è nullo
     * @throws IllegalStateException se l'utente ha già una libreria con lo stesso nome
     */
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


    /**
     * Aggiunge un libro a una libreria personale specificata.
     * Verifica autenticazione e presenza del libro nella libreria.
     *
     * @param req richiesta contenente gli id della libreria e del libro
     * @param userId id dell'utente autenticato
     * @throws AuthenticationException se {@code userId} è nullo
     * @throws IllegalStateException se il libro è già presente nella libreria
     * @throws NoSuchElementException se la libreria non esiste
     */
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

    
    /**
     * Recupera i libri presenti in una libreria personale.
     *
     * @param libId id della libreria
     * @param userId id dell'utente autenticato
     * @return lista di {@link BookDTO}
     * @throws AuthenticationException se {@code userId} è nullo
     * @throws NoSuchElementException se la libreria non esiste
     */
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


    /**
     * Elimina una libreria personale identificata da {@code libId}.
     *
     * @param libId id della libreria da eliminare
     * @param userId id dell'utente autenticato
     * @throws AuthenticationException se {@code userId} è nullo
     */
    @Transactional
    public void deletePersonalLibrary(Long libId, Long userId) {

        if(userId == null) {
            throw new AuthenticationException("Authentication required", "PersonalLibraryService.findAllByUserId");
        }
        personalLibraryRepository.deleteById(libId);

    }


    /**
     * Rimuove un libro dalla libreria personale specificata.
     *
     * @param libId id della libreria
     * @param bookId id del libro da rimuovere
     * @param userId id dell'utente autenticato
     * @throws AuthenticationException se {@code userId} è nullo
     * @throws NoSuchElementException se la libreria non esiste
     * @throws IllegalArgumentException se il libro non è presente nella libreria
     */
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
