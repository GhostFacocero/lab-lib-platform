package com.lab_lib.restapi.Exceptions;

public class AuthenticationException extends SecurityException{

    private String message;
    private String method;

    public AuthenticationException(String message) {
        super();
        this.message = message;
    }
    
    public AuthenticationException(String message, String method) {
        super();
        this.method = method;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getMethod() {
        return this.method;
    }

}
