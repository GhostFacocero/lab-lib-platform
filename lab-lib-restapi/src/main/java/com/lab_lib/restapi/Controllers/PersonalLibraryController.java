// Emanuele Contini, matricola 756441
// Emanuele Gobessi, matricola 757599
// Diego Guidi, matricola 758420
// Nicola Curchi, matricola 757786
// Mirko Gurzau, matricola 757925

package com.lab_lib.restapi.Controllers;

import com.lab_lib.restapi.DTO.Book.BookDTO;
import com.lab_lib.restapi.DTO.PersonalLibrary.*;
import com.lab_lib.restapi.Services.PersonalLibraryService;
import com.lab_lib.restapi.Services.BookService;
import com.lab_lib.restapi.Utils.UserContext;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import java.util.List;

import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;


/**
 * Controller REST per la gestione delle librerie personali dell'utente.
 *
 * <p>Richiede autenticazione (Bearer token) per gli endpoint esposti.
 */
@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/personallibraries")
public class PersonalLibraryController {

    private final PersonalLibraryService personalLibraryService;
    private final BookService bookService;

    public PersonalLibraryController(PersonalLibraryService personalLibraryService,
        BookService bookService) {
        this.personalLibraryService = personalLibraryService;
        this.bookService = bookService;
    }

    
    /**
     * Recupera tutte le librerie personali dell'utente autenticato.
     *
     * @return lista di {@link com.lab_lib.restapi.DTO.PersonalLibrary.PersonalLibraryDTO}
     */
    @GetMapping
    public List<PersonalLibraryDTO> getPersonalLibraries() {
        Long userId = UserContext.getCurrentUserId();
        return personalLibraryService.findAllByUserId(userId);
    }


    /**
     * Crea una nuova libreria personale per l'utente autenticato.
     *
     * @param req richiesta con i dati della nuova libreria
     * @return ResponseEntity con codice 201 in caso di successo
     */
    @PostMapping
    public ResponseEntity<String> addLibrary(@RequestBody AddLibraryRequest req) {
        Long userId = UserContext.getCurrentUserId();
        personalLibraryService.addLibrary(req, userId);
        return ResponseEntity.status(201).body("Success");            
    }


    /**
     * Elimina la libreria personale indicata (richiede autenticazione).
     *
     * @param libId identificativo della libreria
     * @return ResponseEntity con codice 200 in caso di successo
     */
    @DeleteMapping("/{libId}")
    public ResponseEntity<String> deleteLibrary(@PathVariable Long libId) {
        Long userId = UserContext.getCurrentUserId();
        personalLibraryService.deletePersonalLibrary(libId, userId);
        return ResponseEntity.status(200).body("Success");
    }


    /**
     * Endpoint di ricerca all'interno di una libreria personale. Accetta filtri
     * opzionali per titolo, autore, categoria di valutazione e valutazione numerica.
     *
     * @param libId identificativo della libreria
     * @param title titolo da cercare (opzionale)
     * @param author autore da cercare (opzionale)
     * @param ratingName nome della categoria di valutazione (opzionale)
     * @param evaluation valore numerico della valutazione (opzionale)
     * @param page pagina di risultati (default 0)
     * @param size dimensione pagina (default 50)
     * @return pagina di {@link BookDTO} che soddisfano i criteri
     */
    @GetMapping("/{libId}/search_books")
    public Page<BookDTO> search(
        @PathVariable Long libId,
        @RequestParam(required = false) String title,
        @RequestParam(required = false) String author,
        @RequestParam(required = false) String ratingName,
        @RequestParam(required = false) Integer evaluation,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "50") int size)
    {
        Long userId = UserContext.getCurrentUserId();
        if(title == null && author == null) {
            return bookService.getBooksByLibId(userId, libId, page, size);
        }
        if(title != null && author == null) {
            return bookService.searchByLibIdAndTitle(userId, libId, title, page, size);
        }
        if(title == null && author != null) {
            return bookService.searchByLibIdAndAuthor(userId, libId, author, page, size);
        }
        if(title != null && author != null) {
            return bookService.searchByLibIdAndTitleAndAuthor(userId, libId, title, author, page, size);
        }
        return null;
    }


    /**
     * Aggiunge un libro alla libreria personale specificata dall'utente autenticato.
     *
     * @param libId identificativo della libreria
     * @param bookId identificativo del libro da aggiungere
     * @return ResponseEntity con codice 201 in caso di successo
     */
    @PostMapping("/{libId}/book/{bookId}")
    public ResponseEntity<String> addBookToLibrary(@PathVariable Long libId, @PathVariable Long bookId) {
        Long userId = UserContext.getCurrentUserId();
        AddBookToLibraryRequest req = new AddBookToLibraryRequest(libId, bookId);
        personalLibraryService.addBookToLibrary(req, userId);
        return ResponseEntity.status(201).body("Success");
    }


    /**
     * Rimuove un libro dalla libreria personale dell'utente autenticato.
     *
     * @param libId identificativo della libreria
     * @param bookId identificativo del libro da rimuovere
     * @return ResponseEntity con codice 200 in caso di successo
     */
    @DeleteMapping("/{libId}/book/{bookId}")
    public ResponseEntity<String> removeBookFromLibrary(@PathVariable Long libId, @PathVariable Long bookId) {
        Long userId = UserContext.getCurrentUserId();
        personalLibraryService.removeBookFromLibrary(libId, bookId, userId);
        return ResponseEntity.status(200).body("success");
    }

}
