// Emanuele Contini, matricola 756441
// Emanuele Gobessi, matricola 757599
// Diego Guidi, matricola 758420
// Nicola Curchi, matricola 757786
// Mirko Gurzau, matricola 757925

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
