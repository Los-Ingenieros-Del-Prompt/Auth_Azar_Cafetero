package com.aguardiantes.azarcafetero.auth_service.domain.port.out;

import com.aguardiantes.azarcafetero.auth_service.domain.model.GoogleUserData;

public interface GoogleAuthPort {

    GoogleUserData verifyToken(String googleToken);
}