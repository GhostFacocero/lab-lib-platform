// Emanuele Contini, matricola 756441
// Emanuele Gobessi, matricola 757599
// Diego Guidi, matricola 758420
// Nicola Curchi, matricola 757786
// Mirko Gurzau, matricola 757925

package com.lab_lib.restapi.Middleware;

import java.io.IOException;
import java.util.UUID;

import org.springframework.context.annotation.Bean;
import org.springframework.web.filter.OncePerRequestFilter;

import com.lab_lib.restapi.Services.UserService;
import com.lab_lib.restapi.Utils.UserContext;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Filtro che estrae il token dall'header Authorization e popola il
 * {@link com.lab_lib.restapi.Utils.UserContext} con l'ID utente corrispondente
 * quando il token è valido.
 *
 * <p>Viene eseguito per ogni richiesta (OncePerRequestFilter). Se non è
 * presente un token valido la richiesta prosegue senza utente autenticato.
 */
public class UserAuthentication extends OncePerRequestFilter{

    private final UserService userService;

    public UserAuthentication(UserService userService) {
        this.userService = userService;
    }

    @Bean
    public UserAuthentication userAuthentication() {
        return new UserAuthentication(userService);
    }

    /**
     * Estrae il token dall'header, verifica la sua esistenza e imposta il
     * contesto utente per la durata della richiesta.
     *
     * @param request richiesta HTTP corrente
     * @param response risposta HTTP corrente
     * @param filterChain catena di filtri da proseguire
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
            
        try {
            UUID token = extractToken(request);

            //controllo se il token non esiste o non è valido
            if(token == null || !userService.existsByToken(token)) {
                //in caso non faccio nulla e vado avanti col flusso
                filterChain.doFilter(request, response);
                return;
            } else if(token != null && userService.existsByToken(token)) {
                //se il token esiste ed è valido aggiorno il context per questa richiesta
                Long userId = userService.getUserIdByToken(token);
                UserContext.setCurrentUserId(userId);
                filterChain.doFilter(request, response);
            }
        } finally {
            UserContext.clear();
        }
        
    }

    /**
     * Estrae il token Bearer dall'header Authorization se presente.
     *
     * @param req richiesta HTTP
     * @return UUID del token oppure {@code null} se non presente o malformato
     */
    private UUID extractToken(HttpServletRequest req) {
        String authHeader = req.getHeader("Authorization");
        if(authHeader != null && authHeader.startsWith("Bearer ")) {
            return UUID.fromString(authHeader.substring(7));
        }
        return null; // fallback: token in query param
    }
    
}
