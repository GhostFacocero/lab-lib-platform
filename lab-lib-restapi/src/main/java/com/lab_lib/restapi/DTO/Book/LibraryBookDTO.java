// Emanuele Contini, matricola 756441
// Emanuele Gobessi, matricola 757599
// Diego Guidi, matricola 758420
// Nicola Curchi, matricola 757786
// Mirko Gurzau, matricola 757925

package com.lab_lib.restapi.DTO.Book;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO minimale che rappresenta un libro all'interno di una libreria (id + titolo).
 */
@Data
@Getter
@Setter
public class LibraryBookDTO {

    @NotNull
    private Long id;
    
    @NotBlank
    private String title;

    public LibraryBookDTO(Long id, String title) {
        this.id = id;
        this.title = title;
    }

}
