package com.lab_lib.restapi.Exceptions;

public class ViolationException extends SecurityException {
    
    private String message;

    public ViolationException(String message) {
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }

}
