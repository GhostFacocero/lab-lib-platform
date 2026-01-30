// Emanuele Contini, matricola 756441
// Emanuele Gobessi, matricola 757599
// Diego Guidi, matricola 758420
// Nicola Curchi, matricola 757786
// Mirko Gurzau, matricola 757925

package com.lab_lib.restapi.Services;

import com.lab_lib.restapi.Repositories.RatingRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import com.lab_lib.restapi.DTO.Rating.AddRatingToBookRequest;
import com.lab_lib.restapi.DTO.Rating.RatingDTO;
import com.lab_lib.restapi.Exceptions.AuthenticationException;
import com.lab_lib.restapi.Models.Rating;
import com.lab_lib.restapi.Models.RatingName;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Servizio che gestisce le valutazioni (rating) associate ai libri.
 *
 * <p>Fornisce operazioni per elencare le valutazioni di un libro, aggiungere
 * nuove valutazioni da parte di utenti autenticati e cancellare valutazioni
 * esistenti. Collabora con {@link RatingNameService} per validare le categorie
 * di valutazione e con {@link BookService}/{@link UserService} per recuperare
 * entità correlate.
 */
@Service
public class RatingService {

    /** Entity manager per operazioni JPA avanzate. */
    @PersistenceContext
    private EntityManager entityManager;

    /** Repository per le entità Rating. */
    private final RatingRepository ratingRepository;
    /** Servizio per la gestione dei libri correlati alle valutazioni. */
    private final BookService bookService;
    /** Servizio per la gestione degli utenti. */
    private final UserService userService;
    /** Servizio per i nomi/categorie di valutazione. */
    private final RatingNameService ratingNameService;
    

    /**
     * Costruttore del servizio Rating.
     *
     * @param ratingRepository repository dei rating
     * @param bookService servizio libri
     * @param userService servizio utenti
     * @param ratingNameService servizio per categorie di valutazione
     */
    public RatingService(RatingRepository ratingRepository, BookService bookService, UserService userService, RatingNameService ratingNameService) {
        this.ratingRepository = ratingRepository;
        this.bookService = bookService;
        this.userService = userService;
        this.ratingNameService = ratingNameService;
    }


    /**
     * Recupera tutte le valutazioni associate al libro indicato.
     *
     * @param bookId identificatore del libro
     * @return lista di {@link RatingDTO}
     * @throws NoSuchElementException se {@code bookId} è nullo o non ci sono valutazioni
     */
    @Transactional
    public List<RatingDTO> findAllByBookId(Long bookId) {

        if(bookId == null) {
            throw new NoSuchElementException("Missing book");
        } 
        List<Rating> ratings = ratingRepository.findAllByBookId(bookId);
        if(ratings.size() == 0 || ratings == null) {
            throw new NoSuchElementException( "No ratings for this book");
        }
        List<RatingDTO> ratingDTOs = ratings.stream().map(r -> r.toDTO()).toList();
        return ratingDTOs;

    }


    /**
     * Aggiunge una valutazione a un libro da parte di un utente autenticato.
     * Valida che la categoria di valutazione esista e che l'utente non abbia
     * già valutato lo stesso libro con la stessa categoria.
     *
     * @param req payload con i dettagli della valutazione
     * @param bookId identificatore del libro
     * @param userId identificatore dell'utente autenticato
     * @return {@link RatingDTO} della valutazione salvata
     * @throws AuthenticationException se {@code userId} è nullo
     * @throws IllegalArgumentException se la categoria di rating non esiste
     */
    @Transactional
    public RatingDTO addRatingToBook(AddRatingToBookRequest req, Long bookId, Long userId) {

        if(userId == null) {
            throw new AuthenticationException("Authentication required", "RatingService.addRatingToBook");
        }
        String name = req.getName();
        if(!ratingNameService.existsByName(name)) {
            throw new IllegalArgumentException("Selected rating category does not exist");
        }
        Long ratingNameId = ratingNameService.findIdByName(name);
        String review = req.getReview();
        Integer evaluation = req.getEvaluation();
        RatingName ratingName = new RatingName();
        ratingName.setId(ratingNameId);
        ratingName.setName(name);
        if(ratingRepository.existsByBookIdAndNameAndUserId(bookId, ratingName, userId)) {
            throw new IllegalStateException("Selected book is already in this library");
        }
        Rating rating = new Rating();
        rating.setBook(bookService.findBookById(bookId));
        rating.setName(ratingName);
        rating.setUser(userService.findUserById(userId));
        rating.setReview(review);
        rating.setEvaluation(evaluation);
        Rating saved = ratingRepository.save(rating);
        return saved.toDTO();

    }


    /**
     * Elimina una valutazione identificata da {@code ratingId}.
     * Verifica che l'utente sia autenticato.
     *
     * @param ratingId identificativo della valutazione da cancellare
     * @param userId id dell'utente autenticato che richiede la cancellazione
     * @throws AuthenticationException se {@code userId} è nullo
     * @throws NoSuchElementException se la valutazione non esiste
     */
    @Transactional
    public void deleteRating(Long ratingId, Long userId) {

        if(userId == null) {
            throw new AuthenticationException("Authentication required", "RatingService.addRatingToBook");
        }
        if(!ratingRepository.existsById(ratingId)) {
            throw new NoSuchElementException("Rating not found");
        }
        ratingRepository.deleteById(ratingId);

    }
    
}