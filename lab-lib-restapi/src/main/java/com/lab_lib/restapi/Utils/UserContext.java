// Emanuele Contini, matricola 756441
// Emanuele Gobessi, matricola 757599
// Diego Guidi, matricola 758420
// Nicola Curchi, matricola 757786
// Mirko Gurzau, matricola 757925

package com.lab_lib.restapi.Utils;

public class UserContext {

    private static final ThreadLocal<Long> currentUserId = new ThreadLocal<>();

    public static void setCurrentUserId(Long id) {
        currentUserId.set(id);
    }

    public static Long getCurrentUserId() {
        return currentUserId.get();
    }

    public static void clear() {
        currentUserId.remove();
    }
    
}
