package com.lab_lib.restapi.Controllers;

import com.lab_lib.restapi.DTO.Book.BookDTO;
import com.lab_lib.restapi.DTO.Book.RecommendedBookDTO;
import com.lab_lib.restapi.Services.BookService;
import com.lab_lib.restapi.Utils.UserContext;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/books")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }


    /*Per semplificare l'invio di authors e categories al front
    che sono multipli e non presenti direttamente nella tabella
    book, si usano Page<BookDTO> per semplicità di costruzione e pulizia*/
    @GetMapping
    public Page<BookDTO> getBooks(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "50") int size) {
        return bookService.getBooks(page, size);
    }

    @GetMapping("/search")
    public Page<BookDTO> search(
        @RequestParam(required = false) String title,
        @RequestParam(required = false) String author,
        @RequestParam(required = false) String ratingName,
        @RequestParam(required = false) Integer evaluation,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "50") int size)
    {
        if(title != null && author == null) {
            return bookService.searchByTitle(title, page, size);
        }
        if(title == null && author != null) {
            return bookService.searchByAuthor(author, page, size);
        }
        if(title != null && author != null) {
            return bookService.searchByTitleAndAuthor(title, author, page, size);
        }
        if(ratingName != null && evaluation != null) {
            return bookService.searchByRatingNameAndEvaluation(ratingName, evaluation, page, size);
        }
       return null;
    }


    @GetMapping("/{bookId}/recommended_books")
    public List<RecommendedBookDTO> getRecommendedBooks(@PathVariable Long bookId) {
        return bookService.getRecommendedBooks(bookId);
    }


    @SecurityRequirement(name = "bearerAuth")
    @PostMapping("/{bookId}/recommended_book/{recommendedBookId}")
    public ResponseEntity<String> addRecommendedBook(@PathVariable Long bookId, @PathVariable Long recommendedBookId) {
        Long userId = UserContext.getCurrentUserId();
        bookService.addRecommendedBook(bookId, recommendedBookId, userId);
        return ResponseEntity.status(201).body("success");
    }


    @SecurityRequirement(name = "bearerAuth")
    @DeleteMapping("/{bookId}/recommended_book/{recommendedBookId}")
    public ResponseEntity<String> removeRecommendedBook(@PathVariable Long bookId, @PathVariable Long recommendedBookId) {
        Long userId = UserContext.getCurrentUserId();
        bookService.removeRecommendedBook(bookId, recommendedBookId, userId);
        return ResponseEntity.status(200).body("success");
    }

}
