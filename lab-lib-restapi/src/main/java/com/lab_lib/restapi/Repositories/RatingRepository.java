// Emanuele Contini, matricola 756441
// Emanuele Gobessi, matricola 757599
// Diego Guidi, matricola 758420
// Nicola Curchi, matricola 757786
// Mirko Gurzau, matricola 757925

package com.lab_lib.restapi.Repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lab_lib.restapi.Models.Rating;
import com.lab_lib.restapi.Models.RatingName;

/**
 * Repository JPA per l'entità {@link com.lab_lib.restapi.Models.Rating}.
 *
 * <p>Fornisce metodi per recuperare valutazioni per libro e verificare
 * l'esistenza di una valutazione specifica da parte di un utente.
 */
@Repository
public interface RatingRepository extends JpaRepository<Rating, Long> {
    
    /**
     * Recupera tutte le valutazioni associate a un libro identificato dal suo id.
     *
     * @param bookId identificatore del libro
     * @return lista di {@link Rating} associati al libro
     */
    List<Rating> findAllByBookId(Long bookId);
    
    /**
     * Verifica l'esistenza di una valutazione per un determinato libro, categoria
     * di rating e utente.
     *
     * @param bookId identificatore del libro
     * @param name categoria/etichetta del rating ({@link RatingName})
     * @param userId identificatore dell'utente che ha valutato
     * @return {@code true} se esiste almeno una valutazione corrispondente, {@code false} altrimenti
     */
    boolean existsByBookIdAndNameAndUserId(Long bookId, RatingName name, Long userId);

}  