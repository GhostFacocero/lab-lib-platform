package com.lab_lib.restapi.Middleware;

import java.io.IOException;

import org.springframework.web.filter.OncePerRequestFilter;

import com.lab_lib.restapi.Services.UserService;
import com.lab_lib.restapi.Utils.UserContext;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class UserAuthentication extends OncePerRequestFilter{

    private final UserService userService;

    public UserAuthentication(UserService userService) {
        this.userService = userService;
    }

    // TODO Auto-generated method stub
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String token = extractToken(request);


        if(token != null) {
            Long userId = userService.getUserIdByToken(token);

            // Salva l'ID utente nel contesto globale (per questa richiesta)
            UserContext.setCurrentUserId(userId);
        }

        // Prosegui con la catena di filtri
        filterChain.doFilter(request, response);

        // Pulisci sempre il contesto dopo la richiesta
        UserContext.clear();

    }

    private String extractToken(HttpServletRequest req) {
        String authHeader = req.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        return req.getParameter("token"); // fallback: token in query param
    }
    
}
