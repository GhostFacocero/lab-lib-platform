package com.lab_lib.restapi.Services;

public class Common {
    
    protected static String extractRootCauseMessage(Throwable e) {
        Throwable cause = e;
        while (cause.getCause() != null) {
            cause = cause.getCause();
        }
        return cause.getMessage();
    }

}
