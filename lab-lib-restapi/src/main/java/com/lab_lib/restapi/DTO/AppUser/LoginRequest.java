package com.lab_lib.restapi.DTO.AppUser;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data

public class LoginRequest {

    @NotBlank
    private String nickname;

    @NotBlank
    private String password;
}
