// Emanuele Contini, matricola 756441
// Emanuele Gobessi, matricola 757599
// Diego Guidi, matricola 758420
// Nicola Curchi, matricola 757786
// Mirko Gurzau, matricola 757925

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

/**
 * Servizio per le operazioni legate ai libri (ricerca, paginazione e
 * raccomandazioni).
 *
 * <p>Espone metodi per effettuare ricerche per titolo/autore, filtrare per
 * librerie personali e gestire le raccomandazioni associate ai libri.
 * Utilizza {@link BookRepository} per interrogare il database e coordina
 * altre logiche tramite {@link RecommendedBookService} e {@link UserService}.
 */
@Service
public class BookService {

    /** Entity manager per operazioni JPA avanzate come refresh. */
    @PersistenceContext
    private EntityManager entityManager;

    /** Repository per le entità Book. */
    private final BookRepository bookRepository;
    /** Servizio che gestisce le raccomandazioni tra libri. */
    private final RecommendedBookService recommendedBookService;
    /** Servizio per operazioni sugli utenti (controlli di autenticazione). */
    private final UserService userService;


    /**
     * Costruttore del servizio Book.
     *
     * @param bookRepository repository dei libri
     * @param recommendedBookService servizio per le raccomandazioni
     * @param userService servizio per le operazioni utente
     */
    public BookService(BookRepository bookRepository, RecommendedBookService recommendedBookService, UserService userService) {
        this.bookRepository = bookRepository;
        this.recommendedBookService = recommendedBookService;
        this.userService = userService;
    }



    /**
     * Recupera una pagina di libri applicando un limite massimo alla dimensione
     * della pagina per prevenire richieste troppo grandi.
     *
     * @param page indice della pagina (0-based)
     * @param size numero di elementi per pagina (non superiore a 100)
     * @return pagina di {@link BookDTO}
     * @throws IllegalArgumentException se {@code size} supera il limite consentito
     */
    @Transactional
    public Page<BookDTO> getBooks(int page, int size) {

        int maxSize = 100;
        if(size > maxSize) {
            throw new IllegalArgumentException("Page size must not exceed " + maxSize);
        }
        return bookRepository.findAll(PageRequest.of(page, size))
        .map(BookDTO::new);

    }

 
    /**
     * Restituisce una pagina di libri mappata in {@link BookDTO} applicando limiti
     * di dimensione pagina per evitare richieste troppo grandi.
     *
     * @param page indice della pagina (0-based)
     * @param size numero di elementi per pagina (non superiore a 100)
     * @return pagina di {@link BookDTO}
     * @throws IllegalArgumentException se {@code size} supera il limite consentito
     */
    public Page<BookDTO> searchByTitle(String title, int page, int size) {

        int maxSize = 100;
        if(size > maxSize) {
            throw new IllegalArgumentException("Page size must not exceed " + maxSize);
        }

        // Chiamiamo il nuovo metodo "SortedByLength"
        return bookRepository.findByTitleContainingSortedByLength(title, PageRequest.of(page, size))
                .map(BookDTO::new);
    }

    /**
     * Cerca libri il cui titolo inizia con la stringa fornita (ignore case).
     *
     * @param title prefisso del titolo
     * @param page indice della pagina
     * @param size dimensione della pagina
     * @return pagina di {@link BookDTO}
     */
    @Transactional
    public Page<BookDTO> searchByTitleStartsWith(String title, int page, int size) {

        int maxSize = 100;
        if(size > maxSize) {
            throw new IllegalArgumentException("Page size must not exceed " + maxSize);
        }
        return bookRepository.findByTitleStartingWithIgnoreCase(title, PageRequest.of(page, size))
        .map(BookDTO::new);

    }


    /**
     * Cerca libri per nome autore contenente la stringa fornita.
     *
     * @param author parte del nome dell'autore
     * @param page indice della pagina
     * @param size dimensione della pagina
     * @return pagina di {@link BookDTO}
     */
    @Transactional
    public Page<BookDTO> searchByAuthor(String author, int page, int size) {

        int maxSize = 100;
        if(size > maxSize) {
            throw new IllegalArgumentException("Page size must not exceed " + maxSize);
        }
        return bookRepository.findByAuthorsNameContaining(author, PageRequest.of(page, size))
        .map(BookDTO::new);
 

    }

    /**
     * Cerca libri il cui autore inizia con la stringa fornita (ignore case).
     *
     * @param author prefisso nome autore
     * @param page indice della pagina
     * @param size dimensione della pagina
     * @return pagina di {@link BookDTO}
     */
    @Transactional
    public Page<BookDTO> searchByAuthorStartsWith(String author, int page, int size) {

        int maxSize = 100;
        if(size > maxSize) {
            throw new IllegalArgumentException("Page size must not exceed " + maxSize);
        }
        return bookRepository.findByAuthorsNameStartingWithIgnoreCase(author, PageRequest.of(page, size))
        .map(BookDTO::new);
 

    }


    /**
     * Cerca libri il cui titolo o autore iniziano con la stringa fornita.
     * Restituisce risultati distinti per evitare duplicati.
     *
     * @param query prefisso di ricerca
     * @param page indice della pagina
     * @param size dimensione della pagina
     * @return pagina di {@link BookDTO}
     */
    @Transactional
    public Page<BookDTO> searchByTitleOrAuthorStartsWith(String query, int page, int size) {

        int maxSize = 100;
        if(size > maxSize) {
            throw new IllegalArgumentException("Page size must not exceed " + maxSize);
        }
        return bookRepository
            .findDistinctByTitleStartingWithIgnoreCaseOrAuthorsNameStartingWithIgnoreCase(query, query, PageRequest.of(page, size))
            .map(BookDTO::new);
    }


    /**
     * Cerca libri che contengano contemporaneamente le stringhe indicate per titolo e autore.
     *
     * @param title parte del titolo
     * @param author parte del nome dell'autore
     * @param page indice della pagina
     * @param size dimensione della pagina
     * @return pagina di {@link BookDTO}
     */
    @Transactional
    public Page<BookDTO> searchByTitleAndAuthor(String title, String author, int page, int size) {

        int maxSize = 100;
        if(size > maxSize) {
            throw new IllegalArgumentException("Page size must not exceed " + maxSize);
        }
        return bookRepository.findByTitleAndAuthorsNameContaining(title, author, PageRequest.of(page, size))
        .map(BookDTO::new);
        
    }


    /**
     * Cerca libri filtrando per nome della categoria di rating e valore di valutazione.
     *
     * @param name nome della categoria di rating
     * @param evaluation valore numerico della valutazione
     * @param page indice della pagina
     * @param size dimensione della pagina
     * @return pagina di {@link BookDTO}
     */
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


    /**
     * Recupera i libri appartenenti a una libreria personale specifica.
     * Verifica che l'utente sia autenticato.
     *
     * @param userId id dell'utente autenticato
     * @param libId id della libreria personale
     * @param page indice della pagina
     * @param size dimensione della pagina
     * @return pagina di {@link BookDTO}
     * @throws AuthenticationException se {@code userId} è nullo
     */
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


    /**
     * Cerca libri in una libreria personale specifica filtrando per titolo.
     * Verifica autenticazione dell'utente.
     *
     * @param userId id dell'utente
     * @param libId id della libreria
     * @param title parte del titolo
     * @param page indice della pagina
     * @param size dimensione della pagina
     * @return pagina di {@link BookDTO}
     */
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


    /**
     * Cerca libri in una libreria personale specifica filtrando per autore.
     * Verifica autenticazione dell'utente.
     *
     * @param userId id dell'utente
     * @param libId id della libreria
     * @param author parte del nome dell'autore
     * @param page indice della pagina
     * @param size dimensione della pagina
     * @return pagina di {@link BookDTO}
     */
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


    /**
     * Cerca libri in una libreria personale applicando filtri su titolo e autore (ignore case).
     *
     * @param userId id dell'utente
     * @param libId id della libreria
     * @param title parte del titolo
     * @param author parte del nome dell'autore
     * @param page indice della pagina
     * @param size dimensione della pagina
     * @return pagina di {@link BookDTO}
     */
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


    /**
     * Recupera le raccomandazioni associate al libro di id specificato.
     *
     * @param bookId identificatore del libro
     * @return lista di {@link RecommendedBookDTO}
     * @throws NoSuchElementException se il libro non esiste
     */
    @Transactional
    public List<RecommendedBookDTO> getRecommendedBooks(Long bookId) {

        Book book = bookRepository.findById(bookId)
        .orElseThrow(() -> new NoSuchElementException("Book not found"));
        return recommendedBookService.getRecommendedBooks(book);

    }


    /**
     * Aggiunge una raccomandazione (dalla risorsa {@code bookId} verso {@code recommendedBookId})
     * per l'utente specificato. Verifica limiti e coerenze.
     *
     * @param bookId id del libro che riceve la raccomandazione
     * @param recommendedBookId id del libro raccomandato
     * @param userId id dell'utente che effettua la raccomandazione
     * @return {@link RecommendedBookDTO} rappresentante la raccomandazione salvata
     * @throws AuthenticationException se {@code userId} è nullo
     * @throws IllegalArgumentException per input non valido (es. raccomandare lo stesso libro)
     */
    @Transactional
    public RecommendedBookDTO addRecommendedBook(Long bookId, Long recommendedBookId, Long userId) {

        if(userId == null) {
            throw new AuthenticationException("Authentication required", "BookService.addRecommendedBook");
        }
        if(bookId == recommendedBookId) {
            throw new IllegalArgumentException("Cannot recommend the same book");
        }
        Book book = bookRepository.findById(bookId)
        .orElseThrow(() -> new NoSuchElementException("Book not found"));
        Book recommendedBook = bookRepository.findById(recommendedBookId)
        .orElseThrow(() -> new NoSuchElementException("Recommended book not found"));
        AppUser user = userService.findUserById(userId);
        return recommendedBookService.addRecommendedBook(book, recommendedBook, user);

    }


    /**
     * Rimuove una raccomandazione esistente fatta dall'utente.
     *
     * @param bookId id del libro che ha la raccomandazione
     * @param recommendedBookId id del libro raccomandato
     * @param userId id dell'utente che ha effettuato la raccomandazione
     * @throws AuthenticationException se {@code userId} è nullo
     */
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


    /**
     * Recupera un'entità {@link Book} tramite id.
     *
     * @param id identificatore del libro
     * @return l'istanza {@link Book}
     * @throws NoSuchElementException se il libro non viene trovato
     */
    @Transactional
    public Book findBookById(Long id) {
        return bookRepository.findById(id)
        .orElseThrow(() -> new NoSuchElementException("Book not found"));
    }

}