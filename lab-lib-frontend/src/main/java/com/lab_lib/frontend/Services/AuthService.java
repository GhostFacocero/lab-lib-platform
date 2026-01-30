// Emanuele Contini, matricola 756441
// Emanuele Gobessi, matricola 757599
// Diego Guidi, matricola 758420
// Nicola Curchi, matricola 757786
// Mirko Gurzau, matricola 757925

package com.lab_lib.frontend.Services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.inject.Inject;
import com.lab_lib.frontend.Interfaces.IAuthService;
import com.lab_lib.frontend.Models.AuthResponse;
import com.lab_lib.frontend.Models.LoginRequest;
import com.lab_lib.frontend.Models.RegisterRequest;
import com.lab_lib.frontend.Utils.HttpUtil;

/**
 * Implementazione del servizio di autenticazione.
 * Effettua chiamate HTTP al backend per login e registrazione.
 */
public class AuthService implements IAuthService {
    private final HttpUtil httpUtil;

    @Inject
    public AuthService(HttpUtil httpUtil) {
        this.httpUtil = httpUtil;
    }

    @Override
    public AuthResponse login(LoginRequest req) {
        return httpUtil.post("/user/login", req, new TypeReference<AuthResponse>(){});
    }

    @Override
    public AuthResponse register(RegisterRequest req) {
        return httpUtil.post("/user/register", req, new TypeReference<AuthResponse>(){});
    }
}
