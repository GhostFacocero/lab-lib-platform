// Emanuele Contini, matricola 756441
// Emanuele Gobessi, matricola 757599
// Diego Guidi, matricola 758420
// Nicola Curchi, matricola 757786
// Mirko Gurzau, matricola 757925

package com.lab_lib.restapi.Controllers;

import com.lab_lib.restapi.DTO.AppUser.RegisterRequest;
import com.lab_lib.restapi.DTO.AppUser.AuthResponse;
import com.lab_lib.restapi.DTO.AppUser.LoginRequest;
import com.lab_lib.restapi.Services.UserService;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller REST per le operazioni di autenticazione e registrazione utente.
 *
 * <p>Espone endpoint per registrazione (/register) e login (/login).
 */
@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Registra un nuovo utente.
     *
     * @param req DTO con i dati di registrazione
     * @return ResponseEntity contenente {@link AuthResponse} con il token
     */
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest req) {
        UUID token = userService.registerUser(req);
        //usa un DTO come response per pulizia di codice e per inviare il dato come JSON
        //in forma "token:" + token
        return ResponseEntity.status(201).body(new AuthResponse(token));
    }

    /**
     * Autentica l'utente e ritorna il token se le credenziali sono corrette.
     *
     * @param req DTO con nickname e password
     * @return ResponseEntity con {@link AuthResponse} contenente il token
     */
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest req) {
        UUID token = userService.loginUser(req);
        return ResponseEntity.ok().body(new AuthResponse(token));
    }

}
