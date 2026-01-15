package com.lab_lib.restapi.DTO.Book;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class LibraryBookDTO {

    @NotNull
    private Long id;
    
    @NotBlank
    private String title;

    public LibraryBookDTO(Long id, String title) {
        this.id = id;
        this.title = title;
    }

}
