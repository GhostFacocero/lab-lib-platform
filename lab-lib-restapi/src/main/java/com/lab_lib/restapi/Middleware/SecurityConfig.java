package com.lab_lib.restapi.Middleware;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.lab_lib.restapi.Services.UserService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public UserAuthentication userAuthentication(UserService userService) {
        return new UserAuthentication(userService);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, UserAuthentication userAuthentication) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
            .addFilterBefore(userAuthentication, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}