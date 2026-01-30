// Emanuele Contini, matricola 756441
// Emanuele Gobessi, matricola 757599
// Diego Guidi, matricola 758420
// Nicola Curchi, matricola 757786
// Mirko Gurzau, matricola 757925

package com.lab_lib.restapi.Services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lab_lib.restapi.Models.RatingName;
import com.lab_lib.restapi.Repositories.RatingNameRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

/**
 * Servizio che espone utility sulle categorie di valutazione (RatingName).
 *
 * <p>Permette di verificare l'esistenza di una categoria e recuperare il relativo id.
 */
@Service
public class RatingNameService {
    
    /** Entity manager (non usato direttamente ma disponibile per future estensioni). */
    @PersistenceContext
    private EntityManager entityManager;

    /** Repository per le categorie di valutazione. */
    private final RatingNameRepository ratingNameRepository;

    /**
     * Costruttore del servizio.
     *
     * @param ratingNameRepository repository per RatingName
     */
    public RatingNameService(RatingNameRepository ratingNameRepository) {
        this.ratingNameRepository = ratingNameRepository;
    }


    /**
     * Verifica se una categoria con il nome specificato esiste.
     *
     * @param name nome della categoria
     * @return true se esiste, false altrimenti
     */
    @Transactional
    public boolean existsByName(String name) {
        return ratingNameRepository.existsByName(name);
    }


    /**
     * Recupera l'identificativo della categoria a partire dal nome.
     *
     * @param name nome della categoria
     * @return id della categoria
     */
    @Transactional
    public Long findIdByName(String name) {
        RatingName rn = ratingNameRepository.findIdByName(name);
        return rn.getId();
    }

} 
