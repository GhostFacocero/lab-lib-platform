// Emanuele Contini, matricola 756441
// Emanuele Gobessi, matricola 757599
// Diego Guidi, matricola 758420
// Nicola Curchi, matricola 757786
// Mirko Gurzau, matricola 757925

package com.lab_lib.frontend.Exceptions;

public class ApiException extends RuntimeException {
    public ApiException(String message){
        super(message);
    }
    public ApiException(String message, Throwable cause){
        super(message, cause);
    }
}
