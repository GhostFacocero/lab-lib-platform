package com.lab_lib.restapi.DTO.PersonalLibrary;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data

public class GetLibraryBooksRequest {

    @NotNull
    Long libId;

}
