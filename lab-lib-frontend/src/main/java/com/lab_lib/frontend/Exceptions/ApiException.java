package com.lab_lib.frontend.Exceptions;

public class ApiException extends RuntimeException {
    public ApiException(String message, Throwable cause){
        super(message, cause);
    }
}
