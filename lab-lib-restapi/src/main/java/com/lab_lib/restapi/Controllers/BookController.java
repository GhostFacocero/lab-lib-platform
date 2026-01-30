// Emanuele Contini, matricola 756441
// Emanuele Gobessi, matricola 757599
// Diego Guidi, matricola 758420
// Nicola Curchi, matricola 757786
// Mirko Gurzau, matricola 757925

package com.lab_lib.restapi.Controllers;

import com.lab_lib.restapi.DTO.Book.BookDTO;
import com.lab_lib.restapi.DTO.Book.RecommendedBookDTO;
import com.lab_lib.restapi.Services.BookService;
import com.lab_lib.restapi.Utils.UserContext;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller REST per la gestione dei libri.
 *
 * <p>Espone endpoint per:
 * <ul>
 *   <li>paginazione e elenco dei libri;</li>
 *   <li>ricerche avanzate con filtri su titolo, autore e valutazioni;</li>
 *   <li>recupero e gestione delle raccomandazioni (recommended books) correlate a un libro.</li>
 * </ul>
 *
 * <p>Questo controller delega la logica applicativa a {@link BookService} e tratta i parametri HTTP
 * (query parameters e path variables). Le risposte espongono DTO orientati alla vista (`BookDTO`,
 * `RecommendedBookDTO`).
 *
 * @author Team LAB
 * @since 1.0
 * @implNote Le operazioni che modificano lo stato (aggiunta/rimozione di raccomandazioni)
 *            richiedono autenticazione tramite token Bearer (vedi i metodi annotati con
 *            {@code @SecurityRequirement(name = "bearerAuth")}).
 */
@RestController
@RequestMapping("/books")
public class BookController {

    /**
     * Servizio che contiene la logica business relativa ai libri.
     */
    private final BookService bookService;

    /**
     * Costruisce il controller iniettando il servizio dei libri.
     *
     * @param bookService istanza di {@link BookService} fornita dal container
     */
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }


    /**
     * Restituisce una pagina di {@link BookDTO} ordinata secondo la logica del servizio.
     *
     * <p>Supporta paginazione tramite i parametri {@code page} e {@code size}.
     * Valori di default: {@code page=0}, {@code size=50}.
     *
     * @param page indice della pagina da restituire (zero-based)
     * @param size numero di elementi per pagina
     * @return una pagina di {@link BookDTO} (può essere vuota ma non {@code null})
     */
    @GetMapping
    public Page<BookDTO> getBooks(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "50") int size) {
        return bookService.getBooks(page, size);
    }

    /**
     * Endpoint di ricerca avanzata per libri.
     *
     * <p>Permette di combinare filtri su titolo, autore, nome della categoria di valutazione
     * (ratingName) e valore numerico della valutazione ({@code evaluation}). Se {@code startsWith}
     * è true, alcuni filtri (titolo/autore) useranno il match "startsWith" anziché una ricerca"contains".
     *
     * <p>Comportamento:
     * <ul>
     *   <li>Se è fornito solo {@code title} o solo {@code author} verrà eseguita la ricerca corrispondente;</li>
     *   <li>Se sono forniti sia {@code title} che {@code author} verrà applicata la ricerca combinata;</li>
     *   <li>Se sono forniti {@code ratingName} ed {@code evaluation} verrà usata la ricerca per valutazione;</li>
     *   <li>Se nessun filtro è fornito il metodo restituisce {@code null} (comportamento ereditato dal servizio).</li>
     * </ul>
     *
     * @param title titolo da cercare (opzionale)
     * @param author autore da cercare (opzionale)
     * @param ratingName nome della categoria di valutazione (opzionale)
     * @param evaluation valore della valutazione (opzionale)
     * @param startsWith se true usa match "startsWith" per titolo/autore
     * @param page indice della pagina (default 0)
     * @param size dimensione della pagina (default 50)
     * @return pagina di {@link BookDTO} che soddisfano i criteri, o {@code null} se nessun filtro è stato applicato
     * @implNote Restituire {@code null} quando non sono presenti filtri è intenzionale nella logica
     *           corrente; valutare in futuro il ritorno di una pagina vuota per facilitare l'uso lato client.
     */
    @GetMapping("/search")
    public Page<BookDTO> search(
        @RequestParam(required = false) String title,
        @RequestParam(required = false) String author,
        @RequestParam(required = false) String ratingName,
        @RequestParam(required = false) Integer evaluation,
        @RequestParam(defaultValue = "false") boolean startsWith,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "50") int size)
    {
        if(title != null && author == null) {
            return startsWith ? bookService.searchByTitleStartsWith(title, page, size)
                              : bookService.searchByTitle(title, page, size);
        }
        if(title == null && author != null) {
            return startsWith ? bookService.searchByAuthorStartsWith(author, page, size)
                              : bookService.searchByAuthor(author, page, size);
        }
        if(title != null && author != null) {
            return startsWith ? bookService.searchByTitleOrAuthorStartsWith(title, page, size)
                              : bookService.searchByTitleAndAuthor(title, author, page, size);
        }
        if(ratingName != null && evaluation != null) {
            return bookService.searchByRatingNameAndEvaluation(ratingName, evaluation, page, size);
        }
       return null;
    }


    /**
     * Recupera le raccomandazioni associate al libro indicato.
     *
     * @param bookId identificativo del libro da cui prendere le raccomandazioni
     * @return lista di {@link RecommendedBookDTO} correlati al libro
     * @throws IllegalArgumentException se {@code bookId} è {@code null}
     */
    @GetMapping("/{bookId}/recommended_books")
    public List<RecommendedBookDTO> getRecommendedBooks(@PathVariable Long bookId) {
        return bookService.getRecommendedBooks(bookId);
    }


    /**
     * Aggiunge una raccomandazione (book -> recommendedBook) fatta dall'utente autenticato.
     *
     * <p>Richiede autenticazione Bearer; l'ID dell'utente è risolto dal contesto corrente
     * (vedi {@link UserContext#getCurrentUserId()}). L'operazione solleva eccezioni definite
     * nel servizio in caso di risorse non trovate o violazioni di business.
     *
     * @param bookId id del libro che riceve la raccomandazione
     * @param recommendedBookId id del libro raccomandato
     * @return ResponseEntity con codice 201 (Created) in caso di successo
     */
    @SecurityRequirement(name = "bearerAuth")
    @PostMapping("/{bookId}/recommended_book/{recommendedBookId}")
    public ResponseEntity<String> addRecommendedBook(@PathVariable Long bookId, @PathVariable Long recommendedBookId) {
        Long userId = UserContext.getCurrentUserId();
        bookService.addRecommendedBook(bookId, recommendedBookId, userId);
        return ResponseEntity.status(201).body("success");
    }


    /**
     * Rimuove una raccomandazione esistente effettuata dall'utente autenticato.
     *
     * <p>Richiede autenticazione Bearer; se la raccomandazione non esiste il servizio
     * gestirà l'errore adeguatamente (es. sollevando un'eccezione specifica).
     *
     * @param bookId id del libro che possiede la raccomandazione
     * @param recommendedBookId id del libro raccomandato da rimuovere
     * @return ResponseEntity con codice 200 (OK) in caso di successo
     */
    @SecurityRequirement(name = "bearerAuth")
    @DeleteMapping("/{bookId}/recommended_book/{recommendedBookId}")
    public ResponseEntity<String> removeRecommendedBook(@PathVariable Long bookId, @PathVariable Long recommendedBookId) {
        Long userId = UserContext.getCurrentUserId();
        bookService.removeRecommendedBook(bookId, recommendedBookId, userId);
        return ResponseEntity.status(200).body("success");
    }

}
