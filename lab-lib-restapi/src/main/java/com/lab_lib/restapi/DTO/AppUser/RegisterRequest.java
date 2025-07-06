package com.lab_lib.restapi.DTO.AppUser;

import lombok.Data;

@Data

public class RegisterRequest {
    private String nickname;
    private String name;
    private String surname;
    private String cf;
    private String email;
    private String password;
}
