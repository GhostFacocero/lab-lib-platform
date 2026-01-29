package com.lab_lib.frontend.Interfaces;

import java.util.List;
import com.lab_lib.frontend.Models.RatingDTO;

public interface IRatingService {
    void addRatingToBook(long bookId, String name, int evaluation, String review);
    List<RatingDTO> getRatingsByBookId(long bookId);
}
