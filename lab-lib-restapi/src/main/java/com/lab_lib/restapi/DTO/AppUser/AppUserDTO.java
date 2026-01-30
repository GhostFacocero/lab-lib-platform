// Emanuele Contini, matricola 756441
// Emanuele Gobessi, matricola 757599
// Diego Guidi, matricola 758420
// Nicola Curchi, matricola 757786
// Mirko Gurzau, matricola 757925

package com.lab_lib.restapi.DTO.AppUser;

import com.lab_lib.restapi.Models.AppUser;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * DTO pubblico che rappresenta un utente senza esporre dati sensibili
 * (es. password o token).
 */
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
    
    /**
     * Costruisce il DTO a partire dall'entità {@link AppUser}.
     *
     * @param user entità utente
     */
    public AppUserDTO(AppUser user) {
        this.nickname = user.getNickname();
        this.name = user.getName();
        this.surname = user.getSurname();
        this.cf = user.getCf();
        this.email = user.getEmail();
    }

} 
