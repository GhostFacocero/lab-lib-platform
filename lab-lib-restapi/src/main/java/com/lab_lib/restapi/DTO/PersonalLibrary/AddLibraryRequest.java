// Emanuele Contini, matricola 756441
// Emanuele Gobessi, matricola 757599
// Diego Guidi, matricola 758420
// Nicola Curchi, matricola 757786
// Mirko Gurzau, matricola 757925

package com.lab_lib.restapi.DTO.PersonalLibrary;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data

public class AddLibraryRequest {

    @NotBlank
    private String name;
    
}