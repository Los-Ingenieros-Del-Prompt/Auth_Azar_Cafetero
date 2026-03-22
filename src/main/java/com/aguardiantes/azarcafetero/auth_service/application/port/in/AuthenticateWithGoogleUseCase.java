package com.aguardiantes.azarcafetero.auth_service.application.port.in;

import com.aguardiantes.azarcafetero.auth_service.application.dto.AuthenticationResponse;


public interface AuthenticateWithGoogleUseCase {

    AuthenticationResponse authenticate(String idToken);
}