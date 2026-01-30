// Emanuele Contini, matricola 756441
// Emanuele Gobessi, matricola 757599
// Diego Guidi, matricola 758420
// Nicola Curchi, matricola 757786
// Mirko Gurzau, matricola 757925

package com.lab_lib.restapi.Models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.lab_lib.restapi.DTO.Rating.RatingDTO;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;

/**
 * Entità che rappresenta una valutazione lasciata da un utente su un libro.
 *
 * <p>Contiene riferimento al libro, alla categoria di valutazione, all'utente,
 * alla recensione testuale e al valore numerico dell'evaluation (1-5).
 */
@Entity
@Table(name = "rating", uniqueConstraints = @UniqueConstraint(columnNames = {"id_book", "id_rn", "id_user"}))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Rating {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_book")
    @JsonBackReference
    private Book book;

    @ManyToOne
    @JoinColumn(name = "id_rn")
    private RatingName name;

    @ManyToOne
    @JoinColumn(name = "id_user")
    private AppUser user;

    @Column
    private String review;

    @Min(1)
    @Max(5)
    @Column
    private Integer evaluation;

    /**
     * Converte questa entità in un DTO (adatto alla serializzazione e invio al client).
     *
     * @return {@link com.lab_lib.restapi.DTO.Rating.RatingDTO} rappresentante la valutazione
     */
    public RatingDTO toDTO() {
        return new RatingDTO(this);
    }
    
}
