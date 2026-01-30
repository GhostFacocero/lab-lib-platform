// Emanuele Contini, matricola 756441
// Emanuele Gobessi, matricola 757599
// Diego Guidi, matricola 758420
// Nicola Curchi, matricola 757786
// Mirko Gurzau, matricola 757925

package com.lab_lib.restapi.Exceptions;

/**
 * Eccezione lanciata quando un'operazione richiede autenticazione ma
 * non è stata fornita o è invalida.
 *
 * <p>Il campo {@code method} può contenere il nome del metodo che ha richiesto
 * autenticazione (utile per log e diagnosi e per i messaggi di errore al client).
 */
public class AuthenticationException extends SecurityException{

    private String message;
    private String method;

    /**
     * Costruisce l'eccezione con messaggio.
     *
     * @param message descrizione dell'errore
     */
    public AuthenticationException(String message) {
        super();
        this.message = message;
    }
    
    /**
     * Costruisce l'eccezione specificando anche il metodo che richiedeva autenticazione.
     *
     * @param message descrizione dell'errore
     * @param method metodo che ha richiesto autenticazione
     */
    public AuthenticationException(String message, String method) {
        super();
        this.method = method;
    }

    /**
     * Imposta il messaggio descrittivo dell'errore.
     *
     * @param message descrizione dell'errore
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Restituisce il messaggio descrittivo dell'errore.
     *
     * @return messaggio dell'errore
     */
    public String getMessage() {
        return this.message;
    }

    /**
     * Imposta il nome del metodo che ha richiesto autenticazione.
     *
     * @param method nome del metodo (opzionale)
     */
    public void setMethod(String method) {
        this.method = method;
    }

    /**
     * Restituisce il nome del metodo che ha causato l'eccezione di autenticazione.
     *
     * @return nome del metodo (o {@code null} se non specificato)
     */
    public String getMethod() {
        return this.method;
    }

} 
