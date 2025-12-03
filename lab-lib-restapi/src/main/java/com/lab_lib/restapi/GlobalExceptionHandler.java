package com.lab_lib.restapi;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import jakarta.validation.ConstraintViolationException;

import java.time.Instant;
import java.util.*;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Gestione degli errori di validazione sui DTO (@Valid)

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationErrors(MethodArgumentNotValidException ex, WebRequest request) {
        Map<String, String> fieldErrors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(err ->
            fieldErrors.put(err.getField(), err.getDefaultMessage())
        );

        return buildResponse(HttpStatus.BAD_REQUEST, "Validation Error", fieldErrors, request);
    }

    // Gestione dei parametri errati in URL o query string

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Map<String, Object>> handleTypeMismatch(MethodArgumentTypeMismatchException ex, WebRequest request) {
        String message = "Parameter '" + ex.getName() + "' not valid: expected type " + ex.getRequiredType().getSimpleName();
        return buildResponse(HttpStatus.BAD_REQUEST, message, null, request);
    }

    // Gestione errori di constraint (es. @Min, @Max)

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
            request);
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<Map<String, Object>> handleResponseStatusException(ResponseStatusException ex, WebRequest request) {
        HttpStatusCode status = ex.getStatusCode();
        if(status == HttpStatus.UNAUTHORIZED) {
            return buildResponse(
                HttpStatus.UNAUTHORIZED,
                "User is not authenticated",
                null,
                request
            );
        }
        return handleAllExceptions(ex, request);
    }

    // Gestione delle eccezioni generiche (catch universale)

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleAllExceptions(Exception ex, WebRequest request) {
        ex.printStackTrace();

        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR,
                "An internal error occured. Try again later.",
                null,
                request);
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

