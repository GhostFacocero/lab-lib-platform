// Emanuele Contini, matricola 756441
// Emanuele Gobessi, matricola 757599
// Diego Guidi, matricola 758420
// Nicola Curchi, matricola 757786
// Mirko Gurzau, matricola 757925

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
