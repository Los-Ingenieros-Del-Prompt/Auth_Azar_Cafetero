package com.aguardiantes.azarcafetero.auth_service.infrastructure.web;

import com.aguardiantes.azarcafetero.auth_service.domain.exception.GoogleAuthorizationDeniedException;
import com.aguardiantes.azarcafetero.auth_service.domain.exception.InvalidGoogleTokenException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(basePackages = "com.aguardiantes.azarcafetero.auth_service.infrastructure.web")
public class GlobalExceptionHandler {

    @ExceptionHandler(InvalidGoogleTokenException.class)
    public ResponseEntity<ErrorResponse> handleInvalidToken(InvalidGoogleTokenException ex) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(ErrorResponse.of(401, "Unauthorized", ex.getMessage()));
    }

    @ExceptionHandler(GoogleAuthorizationDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAuthDenied(GoogleAuthorizationDeniedException ex) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(ErrorResponse.of(401, "Unauthorized", ex.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneral(Exception ex) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ErrorResponse.of(500, "Internal Server Error", "Error interno, intenta más tarde"));
    }
}