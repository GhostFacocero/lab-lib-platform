package com.lab_lib.restapi.Services;

import com.lab_lib.restapi.Repositories.RatingRepository;

import com.lab_lib.restapi.Models.Rating;
import com.lab_lib.restapi.Models.Book;
import com.lab_lib.restapi.Models.RatingName;


public class RatingService {

    private final RatingRepository ratingRepository;

    public RatingService(RatingRepository ratingRepository) {
        this.ratingRepository = ratingRepository;
    }
    
}