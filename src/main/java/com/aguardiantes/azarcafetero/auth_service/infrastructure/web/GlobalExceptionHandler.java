package com.aguardiantes.azarcafetero.auth_service.infrastructure.web;

// FIX: movido de infrastructure/web/dto → infrastructure/web.
// Razón: GlobalExceptionHandler no es un DTO — es un componente transversal
// de la capa web. Colocarlo dentro del subpaquete "dto" era semánticamente incorrecto
// y dificultaba encontrarlo al navegar el proyecto.

import com.aguardiantes.azarcafetero.auth_service.domain.exception.GoogleAuthorizationDeniedException;
import com.aguardiantes.azarcafetero.auth_service.domain.exception.InvalidGoogleTokenException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InvalidGoogleTokenException.class)
    public ResponseEntity<ErrorResponse> handleInvalidToken(InvalidGoogleTokenException ex) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(ErrorResponse.of(401, "Unauthorized", ex.getMessage()));
    }

    // FIX: código corregido de 400 → 401.
    // Razón: GoogleAuthorizationDeniedException significa que el usuario no autorizó
    // el acceso a Google OAuth — es un problema de autenticación, no de sintaxis
    // de la petición. 400 Bad Request implica que el cliente envió datos mal formados,
    // que no es el caso aquí.
    @ExceptionHandler(GoogleAuthorizationDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAuthDenied(GoogleAuthorizationDeniedException ex) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(ErrorResponse.of(401, "Unauthorized", ex.getMessage()));
    }

    // FIX: código corregido de 502 → 500.
    // Razón: 502 Bad Gateway indica que un servidor upstream (proxy/gateway) recibió
    // una respuesta inválida de otro servidor — un concepto de red, no de aplicación.
    // Los errores inesperados internos deben devolver 500 Internal Server Error.
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneral(Exception ex) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ErrorResponse.of(500, "Internal Server Error", "Error interno, intenta más tarde"));
    }
}
