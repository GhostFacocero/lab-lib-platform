package com.lab_lib.restapi.DTO.AppUser;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data

public class RegisterRequest {

    @NotBlank
    private String nickname;

    @NotBlank
    private String name;

    @NotBlank
    private String surname;

    @NotBlank
    private String cf;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String password;
}
