// Emanuele Contini, matricola 756441
// Emanuele Gobessi, matricola 757599
// Diego Guidi, matricola 758420
// Nicola Curchi, matricola 757786
// Mirko Gurzau, matricola 757925

package com.lab_lib.restapi.DTO.Book;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

import jakarta.validation.constraints.NotBlank;

/**
 * DTO che rappresenta una raccomandazione di un libro, incluso l'elenco
 * dei nickname degli utenti che l'hanno suggerita.
 */
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RecommendedBookDTO {

    @NotBlank
    private String title;

    @NotBlank
    private List<String> userNicknames;
    
} 
