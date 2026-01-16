package com.lab_lib.restapi.DTO.AppUser;

import com.lab_lib.restapi.Models.AppUser;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

//Per evitare di mandare dati sensibili (token e password) al frontend

@Data

public class AppUserDTO {

    @NotBlank
    private String nickname;

    @NotBlank
    private String name;

    @NotBlank
    private String surname;

    @NotBlank
    private String cf;

    @NotBlank
    private String email;
    
    public AppUserDTO(AppUser user) {
        this.nickname = user.getNickname();
        this.name = user.getName();
        this.surname = user.getSurname();
        this.cf = user.getCf();
        this.email = user.getEmail();
    }

}
