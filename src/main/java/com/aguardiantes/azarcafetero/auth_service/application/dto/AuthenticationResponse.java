package com.aguardiantes.azarcafetero.auth_service.application.dto;


public record AuthenticationResponse(
        String token,
        String userId,
        String name,
        String avatarUrl,
        boolean isNewUser
) {}
