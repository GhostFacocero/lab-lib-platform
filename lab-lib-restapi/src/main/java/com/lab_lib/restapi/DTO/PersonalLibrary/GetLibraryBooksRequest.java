// Emanuele Contini, matricola 756441
// Emanuele Gobessi, matricola 757599
// Diego Guidi, matricola 758420
// Nicola Curchi, matricola 757786
// Mirko Gurzau, matricola 757925

package com.lab_lib.restapi.DTO.PersonalLibrary;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * DTO per richieste che richiedono l'elenco dei libri di una libreria.
 */
@Data

public class GetLibraryBooksRequest {

    @NotNull
    Long libId;

}
