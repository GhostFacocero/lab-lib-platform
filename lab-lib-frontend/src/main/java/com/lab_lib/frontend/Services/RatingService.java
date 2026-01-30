// Emanuele Contini, matricola 756441
// Emanuele Gobessi, matricola 757599
// Diego Guidi, matricola 758420
// Nicola Curchi, matricola 757786
// Mirko Gurzau, matricola 757925

package com.lab_lib.frontend.Services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.inject.Inject;
import com.lab_lib.frontend.Interfaces.IRatingService;
import com.lab_lib.frontend.Models.AddRatingToBookRequest;
import com.lab_lib.frontend.Models.RatingDTO;
import com.lab_lib.frontend.Models.RecommendedBookDTO;
import com.lab_lib.frontend.Utils.HttpUtil;
import java.util.List;

public class RatingService implements IRatingService {
    private final HttpUtil httpUtil;

    @Inject
    public RatingService(HttpUtil httpUtil) {
        this.httpUtil = httpUtil;
    }

    @Override
    public void addRatingToBook(long bookId, String name, int evaluation, String review) {
        AddRatingToBookRequest body = new AddRatingToBookRequest(name, evaluation, review);
        String endpoint = "/ratings/book/" + bookId;
        // Backend returns a simple text message; ignore payload and avoid JSON parsing
        httpUtil.postVoid(endpoint, body);
    }

    @Override
    public List<RatingDTO> getRatingsByBookId(long bookId) {
        String endpoint = "/ratings/book/" + bookId;
        return httpUtil.get(endpoint, new TypeReference<List<RatingDTO>>() {});
    }

    @Override
    public void addRatingRecommendation(long bookId, long recommendedBookId) {
        String endpoint = "/books/" + bookId + "/recommended_book/" + recommendedBookId;
        // Backend returns a simple text message; ignore payload and avoid JSON parsing
        httpUtil.postVoid(endpoint, null);
    }

    @Override
    public List<RecommendedBookDTO> getRecommendedBooks(long bookId) {
        String endpoint = "/books/" + bookId + "/recommended_books";
        return httpUtil.get(endpoint, new TypeReference<List<RecommendedBookDTO>>() {});
    }
}
