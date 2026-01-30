// Emanuele Contini, matricola 756441
// Emanuele Gobessi, matricola 757599
// Diego Guidi, matricola 758420
// Nicola Curchi, matricola 757786
// Mirko Gurzau, matricola 757925

package com.lab_lib.restapi.DTO.AppUser;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * DTO per la richiesta di registrazione utente.
 */
@Data

public class RegisterRequest {

    @NotBlank
    private String nickname;

    @NotBlank
    private String name;

    @NotBlank
    private String surname;

    @NotBlank
    private String cf;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String password;
} 
