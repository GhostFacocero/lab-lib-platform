package com.lab_lib.restapi.DTO.AppUser;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

//Per evitare di mandare dati sensibili (token e password) al frontend

@Data

public class AppUserDTO {

    @NotBlank
    private String nickname;
    
}
