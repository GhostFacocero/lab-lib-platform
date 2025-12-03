package com.lab_lib.restapi.Controllers;

import com.lab_lib.restapi.DTO.Book.BookDTO;
import com.lab_lib.restapi.DTO.PersonalLibrary.*;
import com.lab_lib.restapi.Models.Book;
import com.lab_lib.restapi.Models.PersonalLibrary;
import com.lab_lib.restapi.Services.PersonalLibraryService;
import com.lab_lib.restapi.Repositories.UserRepository;
import com.lab_lib.restapi.Services.UserService;
import com.lab_lib.restapi.Utils.UserContext;

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
@RequestMapping("/personallibraries")
public class PersonalLibraryController {

    private final PersonalLibraryService personalLibraryService;
    private final UserService userService;

    public PersonalLibraryController(PersonalLibraryService personalLibraryService, UserService userService) {
        this.personalLibraryService = personalLibraryService;
        this.userService = userService;
    }
    
    @GetMapping
    public List<PersonalLibrary> getPersonalLibraries() {
        Long userId = UserContext.getCurrentUserId();
        return personalLibraryService.findAllByUserId(userId);
    }

    @PostMapping("/add_library")
    public ResponseEntity<String> addLibrary(@RequestBody AddLibraryRequest req) {
        Long userId = UserContext.getCurrentUserId();
        personalLibraryService.addLibrary(req, userId);
        return ResponseEntity.status(201).body("Success");            
    }

    @GetMapping("/get_library_books")
    public List<BookDTO> getLibraryBooks(@RequestParam Long libId) {
        Long userId = UserContext.getCurrentUserId();
        return personalLibraryService.getLibraryBooks(libId, userId);
    }

    @PostMapping("/add_book_to_library")
    public ResponseEntity<String> addBookToLibrary(@RequestBody AddBookToLibraryRequest req) {
        Long userId = UserContext.getCurrentUserId();
        personalLibraryService.addBookToLibrary(req, userId);
        return ResponseEntity.status(201).body("Success");
    }

}
