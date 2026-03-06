package com.aguardiantes.azarcafetero.auth_service.domain.exception;


public class GoogleAuthorizationDeniedException extends RuntimeException {
    public GoogleAuthorizationDeniedException(String message) {
        super(message);
    }
}