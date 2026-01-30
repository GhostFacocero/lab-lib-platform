package com.lab_lib.frontend.Models;

public class RatingDTO {
    private Long id;
    private String bookTitle;
    private String userNickname;
    private String ratingName;
    private String comment;
    private Integer evaluation;

    public RatingDTO() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getBookTitle() { return bookTitle; }
    public void setBookTitle(String bookTitle) { this.bookTitle = bookTitle; }
    public String getUserNickname() { return userNickname; }
    public void setUserNickname(String userNickname) { this.userNickname = userNickname; }
    public String getRatingName() { return ratingName; }
    public void setRatingName(String ratingName) { this.ratingName = ratingName; }
    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }
    public Integer getEvaluation() { return evaluation; }
    public void setEvaluation(Integer evaluation) { this.evaluation = evaluation; }
}