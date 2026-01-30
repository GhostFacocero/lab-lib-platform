// Emanuele Contini, matricola 756441
// Emanuele Gobessi, matricola 757599
// Diego Guidi, matricola 758420
// Nicola Curchi, matricola 757786
// Mirko Gurzau, matricola 757925

package com.lab_lib.restapi.Controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lab_lib.restapi.DTO.Rating.AddRatingToBookRequest;
import com.lab_lib.restapi.DTO.Rating.RatingDTO;
import com.lab_lib.restapi.Services.RatingService;
import com.lab_lib.restapi.Utils.UserContext;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

/**
 * Controller REST per le operazioni sui rating (valutazioni).
 *
 * <p>Espone endpoint per ottenere le valutazioni di un libro, aggiungere una
 * valutazione e cancellare una valutazione. Gli endpoint che modificano lo stato
 * richiedono autenticazione tramite Bearer token (vedi {@link com.lab_lib.restapi.Utils.UserContext}).
 */
@RestController
@RequestMapping("/ratings")
public class RatingController {

    private final RatingService ratingService;
    

    public RatingController(RatingService ratingService) {
        this.ratingService = ratingService;
    }


    /**
     * Recupera la lista di valutazioni associate al libro specificato.
     *
     * @param bookId identificativo del libro
     * @return lista di {@link RatingDTO}
     */
    @GetMapping("/book/{bookId}")
    public List<RatingDTO> getRatingsByBookId(@PathVariable Long bookId) {
        return ratingService.findAllByBookId(bookId);
    }


    /**
     * Aggiunge una valutazione ad un libro. Richiede autenticazione.
     *
     * @param req payload con i dettagli della valutazione
     * @param bookId identificativo del libro
     * @return ResponseEntity con codice 201 in caso di successo
     */
    @SecurityRequirement(name = "bearerAuth")
    @PostMapping("/book/{bookId}")
    public ResponseEntity<String> addRatingToBook(@RequestBody AddRatingToBookRequest req, @PathVariable Long bookId) {
        Long userId = UserContext.getCurrentUserId();
        ratingService.addRatingToBook(req, bookId, userId);
        return ResponseEntity.status(201).body("Success");
    }

    
    /**
     * Elimina una valutazione. Richiede autenticazione.
     *
     * @param ratingId identificativo della valutazione da eliminare
     * @return ResponseEntity con codice 200 in caso di successo
     */
    @SecurityRequirement(name = "bearerAuth")
    @DeleteMapping("/{ratingId}")
    public ResponseEntity<String> deleteRating(@PathVariable Long ratingId) {
        Long userId = UserContext.getCurrentUserId();
        ratingService.deleteRating(ratingId, userId);
        return ResponseEntity.status(200).body("success");
    }
    
}
