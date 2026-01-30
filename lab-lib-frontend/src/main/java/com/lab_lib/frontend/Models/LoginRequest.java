// Emanuele Contini, matricola 756441
// Emanuele Gobessi, matricola 757599
// Diego Guidi, matricola 758420
// Nicola Curchi, matricola 757786
// Mirko Gurzau, matricola 757925

package com.lab_lib.frontend.Models;

/**
 * DTO di richiesta login, compatibile con il backend.
 * Il backend si aspetta `nickname` e `password`.
 */
public class LoginRequest {
    private String nickname;
    private String password;

    public LoginRequest() {}

    public LoginRequest(String nickname, String password) {
        this.nickname = nickname;
        this.password = password;
    }
    public String getNickname() { return nickname; }
    public void setNickname(String nickname) { this.nickname = nickname; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}
