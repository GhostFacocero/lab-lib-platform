// Emanuele Contini, matricola 756441
// Emanuele Gobessi, matricola 757599
// Diego Guidi, matricola 758420
// Nicola Curchi, matricola 757786
// Mirko Gurzau, matricola 757925

package com.lab_lib.restapi.Repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import com.lab_lib.restapi.Models.PersonalLibrary;

/**
 * Repository JPA per l'entità {@link com.lab_lib.restapi.Models.PersonalLibrary}.
 *
 * <p>Definisce query specifiche per le librerie personali, comprese ricerche
 * per utente e verifiche di esistenza di libri all'interno di una libreria.
 */
public interface PersonalLibraryRepository extends JpaRepository<PersonalLibrary, Long> {

    @EntityGraph(attributePaths = "books")
    Optional<PersonalLibrary> findById(Long id);
    
    /**
     * Verifica se esiste una libreria con il nome indicato.
     *
     * @param name nome della libreria
     * @return {@code true} se esiste una libreria con quel nome, {@code false} altrimenti
     */
    boolean existsByName(String name);

    /**
     * Verifica se l'utente specificato ha almeno una libreria personale registrata.
     *
     * @param userId identificatore dell'utente
     * @return {@code true} se l'utente possiede librerie, {@code false} altrimenti
     */
    boolean existsByUserId(Long userId);

    /**
     * Verifica se esiste una libreria con lo stesso nome per lo stesso utente.
     *
     * @param name nome della libreria
     * @param userId identificatore dell'utente proprietario
     * @return {@code true} se è presente una libreria con lo stesso nome per l'utente
     */
    boolean existsByNameAndUserId(String name, Long userId);

    /**
     * Recupera tutte le librerie personali di un utente.
     *
     * @param userId identificatore dell'utente
     * @return lista di {@link PersonalLibrary} appartenenti all'utente
     */
    List<PersonalLibrary> findAllByUserId(Long userId);

    /**
     * Verifica se una libreria (id) contiene un libro specifico (bookId).
     *
     * @param plId identificatore della libreria personale
     * @param bookId identificatore del libro
     * @return {@code true} se il libro è presente nella libreria, {@code false} altrimenti
     */
    boolean existsByIdAndBooksId(Long plId, Long bookId);

} 
