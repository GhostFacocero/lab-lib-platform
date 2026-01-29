package com.lab_lib.frontend.Utils;

import java.time.Duration;
import java.time.Instant;

/**
 * Gestione della sessione utente lato frontend.
 * Conserva il token e verifica scadenza (30 minuti).
 */
public class UserSession {
    private String token;
    private Instant expiresAt;
    private Instant startedAt;
    private String nickname;
    private String displayName; // Nome + Cognome (opzionale)

    /**
     * Imposta il token e la scadenza a 30 minuti da ora.
     */
    public void startSession(String token) {
        this.token = token;
        this.startedAt = Instant.now();
        this.expiresAt = startedAt.plus(Duration.ofMinutes(30));
    }

    /**
     * Ritorna true se esiste un token e non è scaduto.
     */
    public boolean isAuthenticated() {
        return token != null && expiresAt != null && Instant.now().isBefore(expiresAt);
    }

    /**
     * Restituisce il valore da mettere in Authorization: "Bearer <token>".
     * Ritorna null se non autenticato.
     */
    public String getAuthHeader() {
        return isAuthenticated() ? "Bearer " + token : null;
    }

    /**
     * Invalida la sessione (logout manuale o scadenza).
     */
    public void logout() {
        token = null;
        expiresAt = null;
        startedAt = null;
        nickname = null;
        displayName = null;
    }

    // Informazioni non sensibili per UI
    public void setNickname(String nickname) { this.nickname = nickname; }
    public String getNickname() { return nickname; }

    public void setDisplayName(String displayName) { this.displayName = displayName; }
    public String getDisplayName() { return displayName; }

    public Instant getStartedAt() { return startedAt; }
    public Instant getExpiresAt() { return expiresAt; }
}
