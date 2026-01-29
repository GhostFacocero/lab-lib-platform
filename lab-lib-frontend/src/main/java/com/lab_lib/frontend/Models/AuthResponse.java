package com.lab_lib.frontend.Models;

/**
 * Risposta di autenticazione con token.
 * Il backend restituisce un JSON del tipo {"token": "..."}.
 */
public class AuthResponse {
    private String token;

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
}
