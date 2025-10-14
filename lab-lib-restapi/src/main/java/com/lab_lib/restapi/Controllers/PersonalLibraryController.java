package com.lab_lib.restapi.Controllers;

import com.lab_lib.restapi.DTO.PersonalLibrary.AddLibraryRequest;
import com.lab_lib.restapi.Models.PersonalLibrary;
import com.lab_lib.restapi.Services.PersonalLibraryService;
import com.lab_lib.restapi.Repositories.UserRepository;
import com.lab_lib.restapi.Services.UserService;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/PersonalLibraries")
public class PersonalLibraryController {

    private final PersonalLibraryService personalLibraryService;
    private final UserService userService;

    public PersonalLibraryController(PersonalLibraryService personalLibraryService, UserService userService) {
        this.personalLibraryService = personalLibraryService;
        this.userService = userService;
    }
    
    @GetMapping
    public List<PersonalLibrary> getPersonalLibraries(@RequestParam Long userId) {
        if(!userService.existsById(userId)) {
            throw new ResponseStatusException(
                HttpStatus.NOT_FOUND, "User id does not exist"
            );
        }
        return personalLibraryService.findAllByUserId(userId);
    }


    //Trovare un valore con cui sostituire ? tra le parentesi angolari

    @PostMapping("/add_library")
    public ResponseEntity<?> addLibrary(@RequestBody AddLibraryRequest req) {
        personalLibraryService.addLibrary(req);
        return ResponseEntity.status(201).body("Success");            
    }

}
