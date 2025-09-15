package com.lab_lib.restapi.DTO.AppUser;

import lombok.Data;

@Data

public class LoginRequest {
    private String nickname;
    private String password;
}
