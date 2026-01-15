package com.lab_lib.restapi.Controllers;

import com.lab_lib.restapi.DTO.Book.BookDTO;
import com.lab_lib.restapi.DTO.PersonalLibrary.*;
import com.lab_lib.restapi.Models.PersonalLibrary;
import com.lab_lib.restapi.Services.PersonalLibraryService;
import com.lab_lib.restapi.Services.BookService;
import com.lab_lib.restapi.Utils.UserContext;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import java.util.List;

import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;


@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/personallibraries")
public class PersonalLibraryController {

    private final PersonalLibraryService personalLibraryService;
    private final BookService bookService;

    public PersonalLibraryController(PersonalLibraryService personalLibraryService,
        BookService bookService) {
        this.personalLibraryService = personalLibraryService;
        this.bookService = bookService;
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


    @GetMapping("/search_library_books/{libId}")
    public Page<BookDTO> search(
        @PathVariable Long libId,
        @RequestParam(required = false) String title,
        @RequestParam(required = false) String author,
        @RequestParam(required = false) String ratingName,
        @RequestParam(required = false) Integer evaluation,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "50") int size)
    {
        Long userId = UserContext.getCurrentUserId();
        if(title == null && author == null) {
            return bookService.getBooksByLibId(userId, libId, page, size);
        }
        if(title != null && author == null) {
            return bookService.searchByLibIdAndTitle(userId, libId, title, page, size);
        }
        if(title == null && author != null) {
            return bookService.searchByLibIdAndAuthor(userId, libId, author, page, size);
        }
        if(title != null && author != null) {
            return bookService.searchByLibIdAndTitleAndAuthor(userId, libId, title, author, page, size);
        }
        return null;
    }


    @PostMapping("/add_book_to_library/{plId}/{bookId}")
    public ResponseEntity<String> addBookToLibrary(@PathVariable Long plId, @PathVariable Long bookId) {
        Long userId = UserContext.getCurrentUserId();
        AddBookToLibraryRequest req = new AddBookToLibraryRequest(plId, bookId);
        personalLibraryService.addBookToLibrary(req, userId);
        return ResponseEntity.status(201).body("Success");
    }

}
