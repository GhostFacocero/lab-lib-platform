package com.lab_lib.restapi.Controllers;

import com.lab_lib.restapi.DTO.AppUser.RegisterRequest;
import com.lab_lib.restapi.DTO.AppUser.LoginRequest;
import com.lab_lib.restapi.Models.AppUser;
import com.lab_lib.restapi.Models.PersonalLibrary;
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

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest req) {
        try {
            UUID token = userService.registerUser(req);
            return ResponseEntity.status(201).body(Map.of("token", token));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest req) {
        try {
            UUID token = userService.loginUser(req);
            return ResponseEntity.ok().body(Map.of("token", token));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

}
