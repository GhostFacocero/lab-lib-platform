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
