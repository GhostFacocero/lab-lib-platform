package com.lab_lib.restapi.Controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lab_lib.restapi.DTO.Rating.AddRatingToBookRequest;
import com.lab_lib.restapi.Models.Rating;
import com.lab_lib.restapi.Services.RatingService;
import com.lab_lib.restapi.Utils.UserContext;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping("/ratings")
public class RatingController {

    private final RatingService ratingService;
    

    public RatingController(RatingService ratingService) {
        this.ratingService = ratingService;
    }


    @GetMapping("/{bookId}")
    public List<Rating> getRatingsByBookId(@PathVariable Long bookId) {
        return ratingService.findAllByBookId(bookId);
    }


    @SecurityRequirement(name = "bearerAuth")
    @PostMapping("/add_rating/{bookId}")
    public ResponseEntity<String> addRatingToBook(@RequestBody AddRatingToBookRequest req, @PathVariable Long bookId) {
        Long userId = UserContext.getCurrentUserId();
        ratingService.addRatingToBook(req, bookId, userId);
        return ResponseEntity.status(201).body("Success");
    }
    
}
