package com.lab_lib.restapi.Controllers;

import com.lab_lib.restapi.DTO.Book.BookDTO;
import com.lab_lib.restapi.DTO.PersonalLibrary.*;
import com.lab_lib.restapi.Models.PersonalLibrary;
import com.lab_lib.restapi.Services.PersonalLibraryService;
import com.lab_lib.restapi.Utils.UserContext;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import java.util.List;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;


@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/personallibraries")
public class PersonalLibraryController {

    private final PersonalLibraryService personalLibraryService;

    public PersonalLibraryController(PersonalLibraryService personalLibraryService) {
        this.personalLibraryService = personalLibraryService;
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

    @GetMapping("/search_library_books")
    public List<BookDTO> searchLibraryBooks(
        @RequestParam Long libId, 
        @RequestParam(required = false) String title,
        @RequestParam(required = false) String author)
    {
        Long userId = UserContext.getCurrentUserId();
        if(title != null && author == null) {
            return personalLibraryService.searchBookByTitle(libId, userId, title);
        }
        return null;
    }

    @PostMapping("/add_book_to_library")
    public ResponseEntity<String> addBookToLibrary(@RequestBody AddBookToLibraryRequest req) {
        Long userId = UserContext.getCurrentUserId();
        personalLibraryService.addBookToLibrary(req, userId);
        return ResponseEntity.status(201).body("Success");
    }

}
