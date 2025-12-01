package com.lab_lib.restapi.DTO.AppUser;

import lombok.Data;

import java.util.UUID;

@Data

public class AuthResponse {
    private UUID token;

    public AuthResponse(UUID token) {
        this.token = token;
    }

}
