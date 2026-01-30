// Emanuele Contini, matricola 756441
// Emanuele Gobessi, matricola 757599
// Diego Guidi, matricola 758420
// Nicola Curchi, matricola 757786
// Mirko Gurzau, matricola 757925

package com.lab_lib.restapi.DTO.PersonalLibrary;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * DTO che rappresenta la richiesta di aggiungere un libro ad una libreria personale.
 */
@Data

public class AddBookToLibraryRequest {

    @NotNull
    private Long plId;

    @NotNull
    private Long bookId;

    public AddBookToLibraryRequest(Long plId, Long bookId) {
        this.plId = plId;
        this.bookId = bookId;
    }

}