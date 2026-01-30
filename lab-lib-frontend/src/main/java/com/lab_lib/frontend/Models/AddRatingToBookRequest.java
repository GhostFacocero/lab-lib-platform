// Emanuele Contini, matricola 756441
// Emanuele Gobessi, matricola 757599
// Diego Guidi, matricola 758420
// Nicola Curchi, matricola 757786
// Mirko Gurzau, matricola 757925

package com.lab_lib.frontend.Models;

public class AddRatingToBookRequest {
    private String name;
    private String review;
    private Integer evaluation;

    public AddRatingToBookRequest() {}

    public AddRatingToBookRequest(String name, Integer evaluation, String review) {
        this.name = name;
        this.evaluation = evaluation;
        this.review = review;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getReview() { return review; }
    public void setReview(String review) { this.review = review; }
    public Integer getEvaluation() { return evaluation; }
    public void setEvaluation(Integer evaluation) { this.evaluation = evaluation; }
}
