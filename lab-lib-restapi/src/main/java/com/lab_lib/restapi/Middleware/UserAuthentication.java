package com.lab_lib.restapi.Middleware;

import java.io.IOException;
import java.util.UUID;
import java.util.LinkedList;

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

        UUID token = extractToken(request);
        String path = request.getRequestURI();

        //per gli endpoint che non richiedono autenticazione non dà errore se l'autenticazione fallisce
        if(path.contains("/public")) {
            //controllo se il token non esiste o non è valido
            if(token == null || !userService.existsByToken(token)) {
                //in caso non faccio nulla e vado avanti col flusso
                filterChain.doFilter(request, response);
                UserContext.clear();
                return;
            } else if(token != null && userService.existsByToken(token)) {
                //se il token esiste ed è valido aggiorno il context per questa richiesta
                Long userId = userService.getUserIdByToken(token);
                UserContext.setCurrentUserId(userId);
            }
        //per gli endpoint che richiedono autenticazione dà status 401 se l'autenticazione fallisce
        } else {
            if(token == null || !userService.existsByToken(token)) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            } else if(token != null && userService.existsByToken(token)) {
                Long userId = userService.getUserIdByToken(token);
                UserContext.setCurrentUserId(userId);
            }
        }
        //alla fine in ogni caso procedo col flusso e resetto il context
        filterChain.doFilter(request, response);
        UserContext.clear();
    }

    private UUID extractToken(HttpServletRequest req) {
        String authHeader = req.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return UUID.fromString(authHeader.substring(7));
        }
        return UUID.fromString(req.getParameter("token")); // fallback: token in query param
    }
    
}
