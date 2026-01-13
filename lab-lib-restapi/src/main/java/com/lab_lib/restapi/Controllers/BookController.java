package com.lab_lib.restapi.Controllers;

import com.lab_lib.restapi.DTO.Book.BookDTO;
import com.lab_lib.restapi.Services.BookService;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/books")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping
    public Page<BookDTO> getBooks(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "50") int size) {
        return bookService.getBooks(page, size);
    }

    @PostMapping("/search")
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

}
