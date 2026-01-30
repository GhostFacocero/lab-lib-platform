// Emanuele Contini, matricola 756441
// Emanuele Gobessi, matricola 757599
// Diego Guidi, matricola 758420
// Nicola Curchi, matricola 757786
// Mirko Gurzau, matricola 757925

package com.lab_lib.restapi.Exceptions;

/**
 * Eccezione che rappresenta una violazione di accesso o integrità dei dati
 * (es. accesso a risorse private di altri utenti).
 *
 * <p>È gestita centralmente da {@link GlobalExceptionHandler} che la mappa su
 * {@code 403 Forbidden}.
 */
public class ViolationException extends SecurityException {
    
    private String message;

    /**
     * Costruisce l'eccezione con messaggio descrittivo.
     *
     * @param message descrizione dell'errore
     */
    public ViolationException(String message) {
        this.message = message;
    }

    /**
     * Recupera il messaggio descrittivo dell'eccezione.
     *
     * @return messaggio descrittivo
     */
    public String getMessage() {
        return this.message;
    }

} 
