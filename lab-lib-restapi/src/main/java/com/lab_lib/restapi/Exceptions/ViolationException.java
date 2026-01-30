// Emanuele Contini, matricola 756441
// Emanuele Gobessi, matricola 757599
// Diego Guidi, matricola 758420
// Nicola Curchi, matricola 757786
// Mirko Gurzau, matricola 757925

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
