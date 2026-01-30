// Emanuele Contini, matricola 756441
// Emanuele Gobessi, matricola 757599
// Diego Guidi, matricola 758420
// Nicola Curchi, matricola 757786
// Mirko Gurzau, matricola 757925

package com.lab_lib.restapi.Middleware;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.lab_lib.restapi.Services.UserService;

/**
 * Configurazione di sicurezza di Spring Security.
 *
 * <p>Registra il filtro di autenticazione {@link UserAuthentication} e
 * definisce la catena di filtri usata dall'applicazione.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * Bean che espone l'istanza di {@link UserAuthentication} alimentata dal servizio utente.
     *
     * @param userService servizio utenti
     * @return istanza di UserAuthentication
     */
    @Bean
    public UserAuthentication userAuthentication(UserService userService) {
        return new UserAuthentication(userService);
    }

    /**
     * Configura la {@link SecurityFilterChain} disabilitando CSRF e registrando
     * il filtro di autenticazione prima del filtro standard UsernamePassword.
     *
     * @param http builder di sicurezza
     * @param userAuthentication filtro di autenticazione custom
     * @return la SecurityFilterChain costruita
     * @throws Exception in caso di errore di configurazione
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, UserAuthentication userAuthentication) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
            .addFilterBefore(userAuthentication, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}