package com.aguardiantes.azarcafetero.auth_service.domain.port.out;

import com.aguardiantes.azarcafetero.auth_service.domain.model.User;

public interface TokenGeneratorPort {

    String generateToken(User user);
}