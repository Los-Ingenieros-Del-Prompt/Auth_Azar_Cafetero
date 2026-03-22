package com.aguardiantes.azarcafetero.auth_service.application.port.out;

import com.aguardiantes.azarcafetero.auth_service.application.dto.GoogleUserData;

public interface GoogleAuthPort {

    GoogleUserData verifyToken(String googleToken);
}