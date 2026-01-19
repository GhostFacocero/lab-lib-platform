package com.lab_lib.restapi.DTO.PersonalLibrary;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data

public class AddLibraryRequest {

    @NotBlank
    private String name;
    
}