// Emanuele Contini, matricola 756441
// Emanuele Gobessi, matricola 757599
// Diego Guidi, matricola 758420
// Nicola Curchi, matricola 757786
// Mirko Gurzau, matricola 757925

package com.lab_lib.restapi.DTO.AppUser;

import lombok.Data;

import java.util.UUID;

import jakarta.validation.constraints.NotNull;

/**
 * DTO di risposta per operazioni di autenticazione (ritorna il token).
 */
@Data

public class AuthResponse {

    @NotNull
    private UUID token;

    /**
     * Costruisce la risposta con il token.
     *
     * @param token token generato per l'utente
     */
    public AuthResponse(UUID token) {
        this.token = token;
    }

} 
