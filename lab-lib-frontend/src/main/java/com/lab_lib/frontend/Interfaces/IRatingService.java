package com.lab_lib.frontend.Interfaces;

import java.util.List;

import com.lab_lib.frontend.Models.RatingDTO;
import com.lab_lib.frontend.Models.RecommendedBookDTO;

public interface IRatingService {
    void addRatingToBook(long bookId, String name, int evaluation, String review);
    List<RatingDTO> getRatingsByBookId(long bookId);
    void addRatingRecommendation(long bookId, long recommendedBookId);
    List<RecommendedBookDTO> getRecommendedBooks(long bookId);
}
