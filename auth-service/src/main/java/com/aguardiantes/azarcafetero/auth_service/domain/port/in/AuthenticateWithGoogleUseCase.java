package com.aguardiantes.azarcafetero.auth_service.domain.port.in;

public interface AuthenticateWithGoogleUseCase {

    String authenticate(String googleToken);
}