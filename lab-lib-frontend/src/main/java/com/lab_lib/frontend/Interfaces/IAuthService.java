package com.lab_lib.frontend.Interfaces;

import com.lab_lib.frontend.Models.AuthResponse;
import com.lab_lib.frontend.Models.LoginRequest;
import com.lab_lib.frontend.Models.RegisterRequest;

/**
 * Servizio di autenticazione (login e registrazione).
 * Definito come interfaccia per favorire test e sostituzioni.
 */
public interface IAuthService {
    /**
     * Effettua il login con nickname e password.
     * @param req richiesta di login
     * @return risposta con token di autenticazione
     */
    AuthResponse login(LoginRequest req);

    /**
     * Registra un nuovo utente salvando tutti i parametri.
     * @param req richiesta di registrazione
     * @return risposta con token di autenticazione
     */
    AuthResponse register(RegisterRequest req);
}
