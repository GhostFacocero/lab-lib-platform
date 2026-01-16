package com.lab_lib.restapi.DTO.Book;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

import jakarta.validation.constraints.NotBlank;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RecommendedBookDTO {

    @NotBlank
    private String title;

    @NotBlank
    private List<String> userNicknames;
    
}
