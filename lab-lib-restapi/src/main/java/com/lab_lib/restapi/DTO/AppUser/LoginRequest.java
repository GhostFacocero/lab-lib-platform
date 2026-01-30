// Emanuele Contini, matricola 756441
// Emanuele Gobessi, matricola 757599
// Diego Guidi, matricola 758420
// Nicola Curchi, matricola 757786
// Mirko Gurzau, matricola 757925

package com.lab_lib.restapi.DTO.AppUser;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * DTO per la richiesta di login contenente nickname e password.
 */
@Data

public class LoginRequest {

    @NotBlank
    private String nickname;

    @NotBlank
    private String password;
} 
