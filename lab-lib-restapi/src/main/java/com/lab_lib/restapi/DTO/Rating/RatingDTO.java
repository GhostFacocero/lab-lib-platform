// Emanuele Contini, matricola 756441
// Emanuele Gobessi, matricola 757599
// Diego Guidi, matricola 758420
// Nicola Curchi, matricola 757786
// Mirko Gurzau, matricola 757925

package com.lab_lib.restapi.DTO.Rating;

import com.lab_lib.restapi.Models.Rating;

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

    public RatingDTO(Rating rating) {
        this.id = rating.getId();
        this.bookTitle = rating.getBook().getTitle();
        this.userNickname = rating.getUser().getNickname();
        this.ratingName = rating.getName().getName();
        this.comment = rating.getReview();
        this.evaluation = rating.getEvaluation();        
    }

    public RatingDTO(Long id, String bookTitle, String userNickname, String ratingName, String comment, Integer evaluation) {
        this.id = id;
        this. bookTitle = bookTitle;
        this.userNickname = userNickname;
        this.ratingName = ratingName;
        this.comment = comment;
        this.evaluation = evaluation;
    }
    
}
