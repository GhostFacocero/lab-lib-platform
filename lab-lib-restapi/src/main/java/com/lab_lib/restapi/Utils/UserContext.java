// Emanuele Contini, matricola 756441
// Emanuele Gobessi, matricola 757599
// Diego Guidi, matricola 758420
// Nicola Curchi, matricola 757786
// Mirko Gurzau, matricola 757925

package com.lab_lib.restapi.Utils;

/**
 * Contesto legato al thread corrente che memorizza l'identificativo numerico
 * (Long) dell'utente autenticato in corso di esecuzione.
 *
 * <p>Questa classe utilizza un {@code ThreadLocal<Long>} per mantenere lo stato
 * dell'utente su base per-thread: è pensata per essere utilizzata all'interno
 * del ciclo di vita di una richiesta (per esempio da middleware o filtri) per
 * associare l'ID dell'utente al thread che elabora la richiesta. È responsabilità
 * del chiamante impostare e rimuovere il valore (tramite {@link #setCurrentUserId(Long)}
 * e {@link #clear()}) per evitare perdite di memoria o dati residui tra richieste.
 *
 * <p>La classe è composta esclusivamente da metodi statici e non richiede
 * istanziazione. È thread-safe grazie all'uso di {@code ThreadLocal}.
 *
 * @implNote Non memorizzare oggetti pesanti o riferimenti a risorse esterne in questo
 *           contesto: memorizzare solo l'ID (Long) per avere un ciclo di vita breve
 *           e facilmente gestibile.
 */
public class UserContext {

    /**
     * ThreadLocal che contiene l'ID dell'utente corrente per il thread.
     *
     * <p>Visibilità privata: l'accesso deve avvenire solo tramite i metodi statici
     * esposti dalla classe per garantire un corretto ciclo di vita del valore.
     */
    private static final ThreadLocal<Long> currentUserId = new ThreadLocal<>();

    /**
     * Imposta l'ID dell'utente per il thread corrente.
     *
     * @param id l'identificativo numerico dell'utente autenticato; può essere
     *           {@code null} per indicare assenza di utente autenticato
     */
    public static void setCurrentUserId(Long id) {
        currentUserId.set(id);
    }

    /**
     * Recupera l'ID dell'utente associato al thread corrente.
     *
     * @return l'ID dell'utente (Long) oppure {@code null} se non impostato
     */
    public static Long getCurrentUserId() {
        return currentUserId.get();
    }

    /**
     * Rimuove il valore memorizzato nel {@code ThreadLocal} per il thread corrente.
     *
     * <p>Chiamare sempre questo metodo al termine della gestione della richiesta per
     * evitare che il valore persista e venga erroneamente riutilizzato da un thread
     * nel pool di thread.
     */
    public static void clear() {
        currentUserId.remove();
    }
    
}
