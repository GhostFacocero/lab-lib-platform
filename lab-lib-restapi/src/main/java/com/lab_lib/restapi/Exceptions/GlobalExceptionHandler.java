// Emanuele Contini, matricola 756441
// Emanuele Gobessi, matricola 757599
// Diego Guidi, matricola 758420
// Nicola Curchi, matricola 757786
// Mirko Gurzau, matricola 757925

package com.lab_lib.restapi.Exceptions;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import jakarta.validation.ConstraintViolationException;

import java.time.Instant;
import java.util.*;

/**
 * Gestore globale delle eccezioni per i controller REST.
 *
 * <p>Mappa diverse eccezioni su risposte HTTP con payload strutturato
 * contenente timestamp, codice di stato, messaggio d'errore e dettagli.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Gestisce gli errori di validazione dei DTO annotati con {@code @Valid}.
     *
     * <p>Aggrega gli errori di campo e costruisce una risposta HTTP 400 con
     * i dettagli per ciascun campo non valido, utile per il client per mostrare
     * messaggi specifici all'utente.
     *
     * @param ex eccezione che contiene i dettagli di binding/validation
     * @param request contesto della richiesta HTTP
     * @return {@link ResponseEntity} con payload contenente i campi in errore
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationErrors(MethodArgumentNotValidException ex, WebRequest request) {
        Map<String, String> fieldErrors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(err ->
            fieldErrors.put(err.getField(), err.getDefaultMessage())
        );

        return buildResponse(
            HttpStatus.BAD_REQUEST,
            "Validation Error",
            fieldErrors,
            request
        );
    }

    /**
     * Gestisce gli errori di tipo dei parametri (es. query parameter con tipo non valido).
     *
     * Restituisce HTTP 400 con un messaggio che indica il parametro errato e il tipo atteso.
     *
     * @param ex eccezione che descrive il parametro e il tipo richiesto
     * @param request contesto della richiesta HTTP
     * @return {@link ResponseEntity} con messaggio di errore sintetico
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Map<String, Object>> handleTypeMismatch(MethodArgumentTypeMismatchException ex, WebRequest request) {
        String message = "Parameter '" + ex.getName() + "' not valid: expected type " + ex.getRequiredType().getSimpleName();
        return buildResponse(
            HttpStatus.BAD_REQUEST,
            message,
            null,
            request
        );
    }

    /**
     * Gestisce le violazioni di constraint (es. {@code @Min}, {@code @Max}, ecc.).
     *
     * Aggrega le violazioni e ritorna HTTP 400 con una mappa delle proprietà
     * coinvolte e i rispettivi messaggi di violazione.
     *
     * @param ex eccezione contenente le constraint violations
     * @param request contesto della richiesta HTTP
     * @return {@link ResponseEntity} con dettagli delle violazioni
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Map<String, Object>> handleConstraintViolation(ConstraintViolationException ex, WebRequest request) {
        Map<String, String> errors = new HashMap<>();
        ex.getConstraintViolations().forEach(v ->
            errors.put(v.getPropertyPath().toString(), v.getMessage())
        );
        return buildResponse(
            HttpStatus.BAD_REQUEST,
            "Constraint Violation",
            errors,
            request
        );
    }

    /**
     * Gestisce {@link IllegalArgumentException} solitamente generate per input non valido.
     *
     * Ritorna HTTP 400 con il messaggio fornito dall'eccezione.
     *
     * @param ex eccezione che descrive la condizione di input non valido
     * @param request contesto della richiesta HTTP
     * @return {@link ResponseEntity} con messaggio di errore
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalArgumentException(IllegalArgumentException ex, WebRequest request) {
        return buildResponse(
            HttpStatus.BAD_REQUEST,
            ex.getMessage(),
            null,
            request
        );
    }

    /**
     * Gestisce errori per risorse non trovate (es. entità mancanti).
     *
     * Ritorna HTTP 404 con il messaggio contenuto nell'eccezione.
     *
     * @param ex eccezione che descrive la risorsa non trovata
     * @param request contesto della richiesta HTTP
     * @return {@link ResponseEntity} con codice 404
     */
    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<Map<String, Object>> handleNoSuchElementException(NoSuchElementException ex, WebRequest request) {
        return buildResponse(
            HttpStatus.NOT_FOUND,
            ex.getMessage(),
            null,
            request
        );
    }

    /**
     * Gestisce situazioni di stato non valido o conflitti (es. duplicati).
     *
     * Ritorna HTTP 409 (Conflict) con il messaggio dell'eccezione.
     *
     * @param ex eccezione che descrive il conflitto
     * @param request contesto della richiesta HTTP
     * @return {@link ResponseEntity} con codice 409
     */
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalStateException(IllegalStateException ex, WebRequest request) {
        return buildResponse(
            HttpStatus.CONFLICT,
            ex.getMessage(),
            null,
            request
        );
    }

    /**
     * Gestisce violazioni di accesso (tentativi di accesso a dati non autorizzati).
     *
     * Ritorna HTTP 403 (Forbidden) con il messaggio dell'eccezione.
     *
     * @param ex eccezione che descrive la violazione di accesso
     * @param request contesto della richiesta HTTP
     * @return {@link ResponseEntity} con codice 403
     */
    @ExceptionHandler(ViolationException.class)
    public ResponseEntity<Map<String, Object>> handleViolationException(ViolationException ex, WebRequest request) {
        return buildResponse(
            HttpStatus.FORBIDDEN,
            ex.getMessage(),
            null,
            request
        );
    }

    /**
     * Gestisce errori di autenticazione (es. token mancante o invalido).
     *
     * Ritorna HTTP 401 (Unauthorized) e include il metodo che richiedeva autenticazione
     * nei dettagli della risposta.
     *
     * @param ex eccezione di autenticazione che contiene il metodo protetto
     * @param request contesto della richiesta HTTP
     * @return {@link ResponseEntity} con codice 401
     */
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<Map<String, Object>> handleAuthenticationException(AuthenticationException ex, WebRequest request) {
        return buildResponse(
            HttpStatus.UNAUTHORIZED,
            ex.getMessage(),
            ex.getMethod() + " requires authentication",
            request
        );
    }

    /**
     * Gestore globale di fallback per qualsiasi eccezione non catturata specificamente.
     *
     * <p>Logga lo stack trace e ritorna HTTP 500 con un messaggio generico da mostrare al client.
     *
     * @param ex eccezione non gestita specificamente
     * @param request contesto della richiesta HTTP
     * @return {@link ResponseEntity} con codice 500 e messaggio generico
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleAllExceptions(Exception ex, WebRequest request) {
        ex.printStackTrace();
        return buildResponse(
            HttpStatus.INTERNAL_SERVER_ERROR,
            "An internal error occured. Try again later.",
            "No further details",
            request
        );
    }

    /**
     * Costruisce la risposta JSON usata per tutte le eccezioni gestite.
     *
     * @param status codice HTTP
     * @param message messaggio di errore sintetico
     * @param details dettagli aggiuntivi (es. errori di validazione)
     * @param request richiesta web per recuperare il path
     * @return ResponseEntity con payload strutturato
     */
    private ResponseEntity<Map<String, Object>> buildResponse(HttpStatus status, String message, Object details, WebRequest request) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", Instant.now().toString());
        body.put("status", status.value());
        body.put("error", message);
        body.put("details", details);
        body.put("path", request.getDescription(false).replace("uri=", ""));
        return new ResponseEntity<>(body, status);
    }
    
}

