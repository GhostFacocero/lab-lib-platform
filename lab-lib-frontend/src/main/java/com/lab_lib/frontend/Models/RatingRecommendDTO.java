package com.lab_lib.frontend.Models;

public class RatingRecommendDTO {
    private long bookId;
    private long recommendedBookId;

    public RatingRecommendDTO(long bookId, long recommendedBookId) {
        this.bookId = bookId;
        this.recommendedBookId = recommendedBookId;
    }

    public long getBookId() {
        return bookId;
    }

    public long getRecommendedBookId() {
        return recommendedBookId;
    }
}
