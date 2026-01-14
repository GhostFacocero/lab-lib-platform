package com.lab_lib.restapi.Exceptions;

public class AuthenticationException extends SecurityException{

    private String method;

    public AuthenticationException(String message) {
        super();
    }
    
    public AuthenticationException(String message, String method) {
        super();
        this.method = method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getMethod() {
        return this.method;
    }

}
