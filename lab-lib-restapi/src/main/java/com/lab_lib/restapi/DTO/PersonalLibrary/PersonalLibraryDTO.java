package com.lab_lib.restapi.DTO.PersonalLibrary;

import java.util.List;

import com.lab_lib.restapi.DTO.Book.LibraryBookDTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class PersonalLibraryDTO {

    @NotNull
    private Long id;

    @NotBlank
    private String name;

    @NotBlank
    private String userNickname;

    @NotEmpty
    private List<LibraryBookDTO> books;
    
}
