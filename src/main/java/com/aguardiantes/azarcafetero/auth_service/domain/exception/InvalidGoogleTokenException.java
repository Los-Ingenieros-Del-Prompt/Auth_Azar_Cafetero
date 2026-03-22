package com.aguardiantes.azarcafetero.auth_service.domain.exception;


public class InvalidGoogleTokenException extends RuntimeException {
    public InvalidGoogleTokenException(String message) {
        super(message);
    }
}