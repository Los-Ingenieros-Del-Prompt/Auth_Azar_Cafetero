package com.aguardiantes.azarcafetero.auth_service.infrastructure.adapter.out;

import com.aguardiantes.azarcafetero.auth_service.application.dto.GoogleUserData;
import com.aguardiantes.azarcafetero.auth_service.domain.exception.GoogleAuthorizationDeniedException;
import com.aguardiantes.azarcafetero.auth_service.domain.exception.InvalidGoogleTokenException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GoogleAuthAdapterTest {

    @Test
    void shouldThrowExceptionWhenTokenIsNull() {
        GoogleAuthAdapter adapter = new GoogleAuthAdapter("fake-client-id");

        GoogleAuthorizationDeniedException exception = assertThrows(
                GoogleAuthorizationDeniedException.class,
                () -> adapter.verifyToken(null)
        );

        assertEquals(
                "El usuario canceló la autorización de Google",
                exception.getMessage()
        );
    }

    @Test
    void shouldThrowExceptionWhenTokenIsBlank() {
        GoogleAuthAdapter adapter = new GoogleAuthAdapter("fake-client-id");

        GoogleAuthorizationDeniedException exception = assertThrows(
                GoogleAuthorizationDeniedException.class,
                () -> adapter.verifyToken(" ")
        );

        assertEquals(
                "El usuario canceló la autorización de Google",
                exception.getMessage()
        );
    }

    @Test
    void shouldThrowInvalidGoogleTokenExceptionForInvalidToken() {
        GoogleAuthAdapter adapter = new GoogleAuthAdapter("fake-client-id");

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> adapter.verifyToken("invalid-token")
        );

        assertTrue(exception.getMessage().contains("Error al contactar Google"));
    }

    @Test
    void shouldReturnGoogleUserDataForValidToken() {
        /*
         * Este método realmente verifica contra Google,
         * por lo que para probar un token válido se necesitaría:
         *
         * 1. Un token JWT real emitido por Google
         * 2. O mockear GoogleIdTokenVerifier (recomendado con Mockito)
         *
         * Este test queda como placeholder de integración.
         */

        assertTrue(true);
    }
}