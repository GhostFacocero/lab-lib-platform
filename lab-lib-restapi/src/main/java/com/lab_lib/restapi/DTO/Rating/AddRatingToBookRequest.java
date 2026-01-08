package com.lab_lib.restapi.DTO.Rating;

import lombok.Data;

@Data

public class AddRatingToBookRequest {
    String ratingName;
    String review;
    Integer evaluation;
}
