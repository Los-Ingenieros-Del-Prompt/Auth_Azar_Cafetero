package com.aguardiantes.azarcafetero.auth_service.infrastructure.security;

public interface JwtValidatorPort {
    String extractUserId(String token);   // lanza excepción si es inválido/expirado
    boolean isValid(String token);
}