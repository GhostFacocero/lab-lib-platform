// Emanuele Contini, matricola 756441
// Emanuele Gobessi, matricola 757599
// Diego Guidi, matricola 758420
// Nicola Curchi, matricola 757786
// Mirko Gurzau, matricola 757925

package com.lab_lib.restapi.DTO.AppUser;

import lombok.Data;

import java.util.UUID;

import jakarta.validation.constraints.NotNull;

@Data

public class AuthResponse {

    @NotNull
    private UUID token;

    public AuthResponse(UUID token) {
        this.token = token;
    }

}
