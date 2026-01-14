package com.lab_lib.restapi.Exceptions;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import jakarta.validation.ConstraintViolationException;

import java.time.Instant;
import java.util.*;

@RestControllerAdvice
public class GlobalExceptionHandler {

    //Gestione degli errori di validazione sui DTO (@Valid)
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

    //Gestione dei parametri errati in URL o query string
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

    //Gestione errori di constraint (es. @Min, @Max)
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

    //Gestione errori di parametri invalidi o conflittuali
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalArgumentException(IllegalArgumentException ex, WebRequest request) {
        return buildResponse(
            HttpStatus.BAD_REQUEST,
            ex.getMessage(),
            null,
            request
        );
    }

    //Gestione errori per oggetti non trovati
    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<Map<String, Object>> handleNoSuchElementException(NoSuchElementException ex, WebRequest request) {
        return buildResponse(
            HttpStatus.NOT_FOUND,
            ex.getMessage(),
            null,
            request
        );
    }

    //Gestione errori per duplicati e conflitti nel database
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalStateException(NoSuchElementException ex, WebRequest request) {
        return buildResponse(
            HttpStatus.CONFLICT,
            ex.getMessage(),
            null,
            request
        );
    }

    //Gestione errore di autenticazione
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<Map<String, Object>> handleSecurityException(AuthenticationException ex, WebRequest request) {
        return buildResponse(
            HttpStatus.UNAUTHORIZED,
            ex.getMessage(),
            ex.getMethod() + "requires authentication",
            request
        );
    }

    //Gestione delle eccezioni generiche (catch universale)
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

    // Metodo comune per formattare la risposta
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

