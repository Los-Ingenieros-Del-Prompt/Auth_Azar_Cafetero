package com.aguardiantes.azarcafetero.auth_service.infrastructure.web;

import com.aguardiantes.azarcafetero.auth_service.domain.exception.GoogleAuthorizationDeniedException;
import com.aguardiantes.azarcafetero.auth_service.domain.exception.InvalidGoogleTokenException;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import static org.junit.jupiter.api.Assertions.*;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void handleInvalidToken_shouldReturn401() {
        InvalidGoogleTokenException ex = new InvalidGoogleTokenException("Token inválido");

        ResponseEntity<ErrorResponse> response = handler.handleInvalidToken(ex);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(401, response.getBody().status());
        assertEquals("Unauthorized", response.getBody().error());
        assertEquals("Token inválido", response.getBody().message());
    }

    @Test
    void handleAuthDenied_shouldReturn401() {
        GoogleAuthorizationDeniedException ex = new GoogleAuthorizationDeniedException("Usuario canceló");

        ResponseEntity<ErrorResponse> response = handler.handleAuthDenied(ex);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(401, response.getBody().status());
        assertEquals("Unauthorized", response.getBody().error());
        assertEquals("Usuario canceló", response.getBody().message());
    }

    @Test
    void handleGeneral_shouldReturn500WithGenericMessage() {
        Exception ex = new RuntimeException("Algo explotó");

        ResponseEntity<ErrorResponse> response = handler.handleGeneral(ex);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(500, response.getBody().status());
        assertEquals("Internal Server Error", response.getBody().error());
        // No expone detalles internos del error original
        assertEquals("Error interno, intenta más tarde", response.getBody().message());
    }
}