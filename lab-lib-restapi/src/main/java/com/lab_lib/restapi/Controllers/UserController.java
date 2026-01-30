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

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest req) {
        UUID token = userService.registerUser(req);
        //usa un DTO come response per pulizia di codice e per inviare il dato come JSON
        //in forma "token:" + token
        return ResponseEntity.status(201).body(new AuthResponse(token));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest req) {
        UUID token = userService.loginUser(req);
        return ResponseEntity.ok().body(new AuthResponse(token));
    }

}
