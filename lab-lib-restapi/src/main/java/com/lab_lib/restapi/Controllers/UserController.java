package com.lab_lib.restapi.Controllers;

import com.lab_lib.restapi.DTO.AppUser.RegisterRequest;
import com.lab_lib.restapi.DTO.AppUser.AuthResponse;
import com.lab_lib.restapi.DTO.AppUser.LoginRequest;
import com.lab_lib.restapi.DTO.AppUser.AuthResponse;
import com.lab_lib.restapi.Models.AppUser;
import com.lab_lib.restapi.Services.UserService;

import java.util.Map;
import java.util.UUID;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/public/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest req) {
        UUID token = userService.registerUser(req);
        //usa un DTO come response per pulizia di codice e per inviare il dato come JSON
        //in forma "token:" + token
        return ResponseEntity.status(201).body(new AuthResponse(token));
    }

    @PostMapping("/public/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest req) {
        UUID token = userService.loginUser(req);
        return ResponseEntity.ok().body(new AuthResponse(token));
    }

}
