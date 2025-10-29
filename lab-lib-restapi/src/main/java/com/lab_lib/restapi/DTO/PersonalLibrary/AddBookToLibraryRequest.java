package com.lab_lib.restapi.DTO.PersonalLibrary;

import lombok.Data;

@Data

public class AddBookToLibraryRequest {
    private Long PlId;
    private Long BookId;
}