// Emanuele Contini, matricola 756441
// Emanuele Gobessi, matricola 757599
// Diego Guidi, matricola 758420
// Nicola Curchi, matricola 757786
// Mirko Gurzau, matricola 757925

package com.lab_lib.restapi.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lab_lib.restapi.Models.RatingName;

/**
 * Repository JPA per l'entità {@link com.lab_lib.restapi.Models.RatingName}.
 *
 * <p>Fornisce metodi per verificare l'esistenza di una categoria di valutazione
 * e per recuperare l'entità tramite il nome.
 */
@Repository
public interface RatingNameRepository extends JpaRepository<RatingName, Long> {
    
    /**
     * Verifica se esiste una categoria di valutazione con il nome specificato.
     *
     * @param name nome della categoria di rating
     * @return {@code true} se la categoria esiste, {@code false} altrimenti
     */
    boolean existsByName(String name);

    /**
     * Recupera la categoria di valutazione corrispondente al nome fornito.
     *
     * @param name nome della categoria
     * @return istanza di {@link RatingName} o {@code null} se non trovata
     */
    RatingName findIdByName(String name);

} 
