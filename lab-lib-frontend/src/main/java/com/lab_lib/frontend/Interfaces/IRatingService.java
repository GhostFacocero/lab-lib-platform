// Emanuele Contini, matricola 756441
// Emanuele Gobessi, matricola 757599
// Diego Guidi, matricola 758420
// Nicola Curchi, matricola 757786
// Mirko Gurzau, matricola 757925

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
