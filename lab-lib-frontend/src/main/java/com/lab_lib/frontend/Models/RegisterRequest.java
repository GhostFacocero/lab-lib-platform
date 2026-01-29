package com.lab_lib.frontend.Models;

/**
 * DTO di richiesta registrazione, compatibile con il backend.
 * Campi richiesti: nickname, name, surname, cf, email, password.
 */
public class RegisterRequest {
    private String nickname;
    private String name;
    private String surname;
    private String cf;
    private String email;
    private String password;

    public String getNickname() { return nickname; }
    public void setNickname(String nickname) { this.nickname = nickname; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getSurname() { return surname; }
    public void setSurname(String surname) { this.surname = surname; }
    public String getCf() { return cf; }
    public void setCf(String cf) { this.cf = cf; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}
