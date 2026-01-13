package com.lab_lib.restapi.Services;

import com.lab_lib.restapi.Repositories.BookRepository;
import com.lab_lib.restapi.Repositories.RatingRepository;
import com.lab_lib.restapi.Repositories.UserRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import com.lab_lib.restapi.DTO.Rating.AddRatingToBookRequest;
import com.lab_lib.restapi.Models.Rating;
import com.lab_lib.restapi.Models.RatingName;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;


@Service
public class RatingService {

    @PersistenceContext
    private EntityManager entityManager;

    private final RatingRepository ratingRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    

    public RatingService(RatingRepository ratingRepository, BookRepository bookRepository, UserRepository userRepository) {
        this.ratingRepository = ratingRepository;
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
    }


    @Transactional
    public List<Rating> findAllByBookId(Long bookId) {

        if(bookId == null) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST, "Missing book"
            );
        } 

        List<Rating> ratings = ratingRepository.findAllByBookId(bookId);
        if(ratings.size() == 0 || ratings == null) {
            throw new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "No rating for this book"
            );
        }

        return ratings;

    }


    @Transactional
    public Rating addRatingToBook(AddRatingToBookRequest req, Long bookId, Long userId) {

        if(userId == null) {
            throw new ResponseStatusException(
                HttpStatus.UNAUTHORIZED, "User is not authenticated"
            );
        }

        String name = req.getName();
        String review = req.getReview();
        Integer evaluation = req.getEvaluation();

        RatingName ratingName = new RatingName();
        ratingName.setName(name);

        if(ratingRepository.existsByBookIdAndNameAndUserId(bookId, ratingName, userId)) {
            throw new ResponseStatusException(
                HttpStatus.CONFLICT,
                "Cannot add multiple ratings for the same rating category"
            );
        }
        
        Rating rating = new Rating();

        rating.setBook(bookRepository.findById(userId)
        .orElseThrow(() -> new ResponseStatusException(
            HttpStatus.NOT_FOUND, 
            "Book not found"
        )));

        rating.setName(ratingName);

        rating.setUser(userRepository.findById(userId)
        .orElseThrow(() -> new ResponseStatusException(
            HttpStatus.NOT_FOUND,
            "User not found"
        )));

        rating.setReview(review);
        rating.setEvaluation(evaluation);

        Rating saved = ratingRepository.save(rating);
        return saved;

    }
    
}