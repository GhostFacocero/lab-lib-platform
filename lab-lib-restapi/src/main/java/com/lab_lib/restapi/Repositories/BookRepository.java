// Emanuele Contini, matricola 756441
// Emanuele Gobessi, matricola 757599
// Diego Guidi, matricola 758420
// Nicola Curchi, matricola 757786
// Mirko Gurzau, matricola 757925

package com.lab_lib.restapi.Repositories;

import com.lab_lib.restapi.Models.Book;
import com.lab_lib.restapi.Models.RatingName;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;

/**
 * Repository JPA per l'entità {@link com.lab_lib.restapi.Models.Book}.
 *
 * <p>Definisce query per ricerche su titolo, autore, filtri per librerie personali
 * e ricerche avanzate per rating e ordinamenti particolari.
 */
@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    @Override
    @EntityGraph(attributePaths = {"publisher"})

    /**
     * Restituisce tutti i libri con l'entità {@code publisher} caricata (EAGER).
     *
     * @param pageable parametri di paginazione e ordinamento
     * @return pagina di {@link Book} con {@code publisher} eager-loaded
     */
    Page<Book> findAll(Pageable pageable);

    /**
     * Cerca libri il cui titolo contiene la stringa specificata (case-insensitive).
     *
     * @param title parte del titolo da cercare
     * @param pageable parametri di paginazione
     * @return pagina di libri che contengono {@code title} nel titolo
     */
    Page<Book> findByTitleContaining(String title, Pageable pageable);

    /**
     * Cerca libri il cui titolo inizia con la stringa specificata (ignorando il case).
     *
     * @param title prefisso del titolo
     * @param pageable parametri di paginazione
     * @return pagina di libri i cui titoli iniziano con {@code title}
     */
    Page<Book> findByTitleStartingWithIgnoreCase(String title, Pageable pageable);

    /**
     * Cerca libri dove il nome di uno degli autori contiene la stringa specificata.
     *
     * @param author parte del nome dell'autore da cercare
     * @param pageable parametri di paginazione
     * @return pagina di libri con autori che contengono {@code author}
     */
    Page<Book> findByAuthorsNameContaining(String author, Pageable pageable);

    /**
     * Cerca libri il cui titolo e il cui autore contengono le stringhe fornite.
     * Entrambe le condizioni devono essere soddisfatte.
     *
     * @param title parte del titolo da cercare
     * @param author parte del nome dell'autore
     * @param pageable parametri di paginazione
     * @return pagina di libri che soddisfano entrambi i criteri
     */
    Page<Book> findByTitleAndAuthorsNameContaining(String title, String author, Pageable pageable);

    /**
     * Cerca libri il cui autore inizia con la stringa specificata (ignorando il case).
     *
     * @param author prefisso del nome dell'autore
     * @param pageable parametri di paginazione
     * @return pagina di libri con autori il cui nome inizia con {@code author}
     */
    Page<Book> findByAuthorsNameStartingWithIgnoreCase(String author, Pageable pageable);

    /**
     * Cerca libri il cui titolo oppure il cui autore iniziano con i prefissi indicati (ignorando il case).
     * Restituisce risultati distinti per evitare duplicati quando più condizioni sono vere.
     *
     * @param title prefisso per il titolo
     * @param author prefisso per il nome dell'autore
     * @param pageable parametri di paginazione
     * @return pagina di libri distinti che corrispondono ai prefissi
     */
    Page<Book> findDistinctByTitleStartingWithIgnoreCaseOrAuthorsNameStartingWithIgnoreCase(String title, String author, Pageable pageable);

    /**
     * Cerca libri presenti in una libreria personale specificata dall'id.
     *
     * @param libId identificatore della libreria personale
     * @param pageable parametri di paginazione
     * @return pagina di libri appartenenti alla libreria specificata
     */
    Page<Book> findByLibrariesId(Long libId, Pageable pageable);

    /**
     * Cerca libri presenti in una libreria specifica il cui titolo contiene la stringa fornita.
     *
     * @param libId identificatore della libreria personale
     * @param title parte del titolo da cercare
     * @param pageable parametri di paginazione
     * @return pagina di libri nella libreria con titoli che contengono {@code title}
     */
    Page<Book> findByLibrariesIdAndTitleContaining(Long libId, String title, Pageable pageable);

    /**
     * Cerca libri presenti in una libreria specifica il cui autore contiene la stringa fornita.
     *
     * @param libId identificatore della libreria personale
     * @param author parte del nome dell'autore
     * @param pageable parametri di paginazione
     * @return pagina di libri nella libreria con autori che contengono {@code author}
     */
    Page<Book> findByLibrariesIdAndAuthorsNameContaining(Long libId, String author, Pageable pageable);

    /**
     * Cerca libri in una libreria specifica applicando filtri sia sul titolo che sull'autore (ignorando il case).
     *
     * @param libId identificatore della libreria personale
     * @param title parte del titolo da cercare
     * @param author parte del nome dell'autore da cercare
     * @param pageable parametri di paginazione
     * @return pagina di libri che corrispondono a entrambi i filtri nella libreria specificata
     */
    Page<Book> findByLibrariesIdAndTitleContainingIgnoreCaseAndAuthorsNameContainingIgnoreCase(Long libId, String title, String author, Pageable pageable);

    /**
     * Cerca libri che hanno un rating con il nome e la valutazione specificati.
     *
     * @param name istanza di {@link RatingName} che rappresenta la categoria del rating
     * @param evaluation valore numerico della valutazione (es. stelle)
     * @param pageable parametri di paginazione
     * @return pagina di libri che hanno il rating specificato
     */
    Page<Book> findByRatingsNameAndRatingsEvaluation(RatingName name, Integer evaluation, Pageable pageable);

    /**
     * Query custom: cerca libri il cui titolo contiene la stringa specificata (case-insensitive)
     * e ordina i risultati in base alla lunghezza del titolo (dal più corto al più lungo).
     *
     * @param title testo da cercare nel titolo
     * @param pageable parametri di paginazione
     * @return pagina di libri ordinata per lunghezza del titolo
     */
    @Query("SELECT b FROM Book b WHERE LOWER(b.title) LIKE LOWER(CONCAT('%', :title, '%')) ORDER BY LENGTH(b.title) ASC")
    Page<Book> findByTitleContainingSortedByLength(@Param("title") String title, Pageable pageable);
}

