// Emanuele Contini, matricola 756441
// Emanuele Gobessi, matricola 757599
// Diego Guidi, matricola 758420
// Nicola Curchi, matricola 757786
// Mirko Gurzau, matricola 757925

package com.lab_lib.restapi.Repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lab_lib.restapi.Models.AppUser;
import com.lab_lib.restapi.Models.Book;
import com.lab_lib.restapi.Models.RecommendedBook;

/**
 * Repository JPA per l'entità {@link com.lab_lib.restapi.Models.RecommendedBook}.
 *
 * <p>Definisce metodi per verificare esistenze, ricerche per libro e conteggio
 * delle raccomandazioni per utente.
 */
@Repository
public interface RecommendedBookRepository extends JpaRepository<RecommendedBook, Long> {

    /**
     * Verifica se esiste una raccomandazione che collega due libri.
     *
     * @param book libro di riferimento
     * @param recommendedBook libro raccomandato
     * @return {@code true} se la relazione di raccomandazione esiste
     */
    boolean existsByBookAndRecommendedBook(Book book, Book recommendedBook);

    /**
     * Recupera tutte le raccomandazioni generate a partire da un libro.
     *
     * @param book libro di riferimento
     * @return lista di {@link RecommendedBook} che contengono raccomandazioni
     */
    List<RecommendedBook> findAllByBook(Book book);

    /**
     * Recupera la raccomandazione che ha come libro raccomandato l'istanza fornita.
     *
     * @param recommendedBook libro che è stato raccomandato
     * @return istanza di {@link RecommendedBook} o {@code null} se non trovata
     */
    RecommendedBook findByRecommendedBook(Book recommendedBook);

    /**
     * Recupera la raccomandazione specifica che collega due libri.
     *
     * @param book libro di riferimento
     * @param recommendedBook libro raccomandato
     * @return istanza di {@link RecommendedBook} o {@code null} se non trovata
     */
    RecommendedBook findByBookAndRecommendedBook(Book book, Book recommendedBook);

    /**
     * Conta quante raccomandazioni esistono per un determinato libro da parte di un utente.
     *
     * @param book libro di riferimento
     * @param user utente per cui contare le raccomandazioni
     * @return numero di raccomandazioni (intero)
     */
    int countByBookAndUsersContains(Book book, AppUser user);

} 
