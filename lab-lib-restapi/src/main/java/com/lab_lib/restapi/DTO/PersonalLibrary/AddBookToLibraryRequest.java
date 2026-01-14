package com.lab_lib.restapi.DTO.PersonalLibrary;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data

public class AddBookToLibraryRequest {

    @NotNull
    private Long plId;

    @NotNull
    private Long bookId;

    public AddBookToLibraryRequest(Long plId, Long bookId) {
        this.plId = plId;
        this.bookId = bookId;
    }

}