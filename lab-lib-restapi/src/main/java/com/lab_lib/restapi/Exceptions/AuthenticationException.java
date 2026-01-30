// Emanuele Contini, matricola 756441
// Emanuele Gobessi, matricola 757599
// Diego Guidi, matricola 758420
// Nicola Curchi, matricola 757786
// Mirko Gurzau, matricola 757925

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
