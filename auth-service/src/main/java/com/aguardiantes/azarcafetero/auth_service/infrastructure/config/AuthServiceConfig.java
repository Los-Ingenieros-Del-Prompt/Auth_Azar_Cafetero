package com.aguardiantes.azarcafetero.auth_service.infrastructure.config;

import com.aguardiantes.azarcafetero.auth_service.application.service.AuthenticateWithGoogleService;
import com.aguardiantes.azarcafetero.auth_service.domain.port.out.GoogleAuthPort;
import com.aguardiantes.azarcafetero.auth_service.domain.port.out.TokenGeneratorPort;
import com.aguardiantes.azarcafetero.auth_service.domain.port.out.UserRepositoryPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AuthServiceConfig {

    @Bean
    public AuthenticateWithGoogleService authenticateWithGoogleService(
            GoogleAuthPort googleAuthPort,
            UserRepositoryPort userRepositoryPort,
            TokenGeneratorPort tokenGeneratorPort
    ) {
        return new AuthenticateWithGoogleService(
                googleAuthPort,
                userRepositoryPort,
                tokenGeneratorPort
        );
    }
}