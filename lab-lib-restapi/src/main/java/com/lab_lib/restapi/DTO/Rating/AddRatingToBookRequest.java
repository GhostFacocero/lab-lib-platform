package com.lab_lib.restapi.DTO.Rating;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data

public class AddRatingToBookRequest {
    @NotNull
    String ratingName;
    @NotNull
    String review;
    @NotNull
    @Min(1)
    @Max(5)
    Integer evaluation;
}
