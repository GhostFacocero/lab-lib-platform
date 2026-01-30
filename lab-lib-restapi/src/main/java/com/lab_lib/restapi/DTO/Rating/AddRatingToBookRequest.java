// Emanuele Contini, matricola 756441
// Emanuele Gobessi, matricola 757599
// Diego Guidi, matricola 758420
// Nicola Curchi, matricola 757786
// Mirko Gurzau, matricola 757925

package com.lab_lib.restapi.DTO.Rating;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * DTO per la richiesta di aggiungere una valutazione ad un libro.
 */
@Data

public class AddRatingToBookRequest {

    @NotBlank
    String name;

    String review;

    @NotNull
    @Min(1)
    @Max(5)
    Integer evaluation;
    
} 
