package com.lab_lib.restapi.DTO.Rating;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class RatingDTO {

    @NotNull
    private Long id;

    @NotBlank
    private String bookTitle;

    @NotBlank
    private String userNickname;

    @NotBlank
    private String ratingName;

    @NotBlank
    private String comment;
    
    @NotNull
    private Integer evaluation;

    public RatingDTO(Long id, String bookTitle, String userNickname, String ratingName, String comment, Integer evaluation) {
        this.id = id;
        this. bookTitle = bookTitle;
        this.userNickname = userNickname;
        this.ratingName = ratingName;
        this.comment = comment;
        this.evaluation = evaluation;
    }
    
}
