// Emanuele Contini, matricola 756441
// Emanuele Gobessi, matricola 757599
// Diego Guidi, matricola 758420
// Nicola Curchi, matricola 757786
// Mirko Gurzau, matricola 757925

package com.lab_lib.restapi.DTO.Rating;

import com.lab_lib.restapi.Models.Rating;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO che rappresenta una valutazione (rating) per l'API.
 *
 * <p>Contiene i dati da inviare al client: titolo del libro, nickname dell'utente,
 * categoria, commento e valore numerico.
 */
@Data
@Getter
@Setter
public class RatingDTO {

    @NotNull
    private Long id;

    @NotBlank
    private String bookTitle;

    @NotBlank
    private String userNickname;

    @NotBlank
    private String ratingName;

    @NotBlank
    private String comment;
    
    @NotNull
    private Integer evaluation;

    /**
     * Costruisce il DTO a partire dall'entità {@link Rating}.
     *
     * @param rating entità di dominio
     */
    public RatingDTO(Rating rating) {
        this.id = rating.getId();
        this.bookTitle = rating.getBook().getTitle();
        this.userNickname = rating.getUser().getNickname();
        this.ratingName = rating.getName().getName();
        this.comment = rating.getReview();
        this.evaluation = rating.getEvaluation();        
    }

    /**
     * Costruttore completo per uso nei test o deserializzazione.
     */
    public RatingDTO(Long id, String bookTitle, String userNickname, String ratingName, String comment, Integer evaluation) {
        this.id = id;
        this. bookTitle = bookTitle;
        this.userNickname = userNickname;
        this.ratingName = ratingName;
        this.comment = comment;
        this.evaluation = evaluation;
    }
    
} 
