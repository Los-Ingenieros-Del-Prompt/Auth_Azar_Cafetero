package com.aguardiantes.azarcafetero.auth_service.infrastructure.adapter.out;

import com.aguardiantes.azarcafetero.auth_service.application.dto.GoogleUserData;
import com.aguardiantes.azarcafetero.auth_service.domain.exception.GoogleAuthorizationDeniedException;
import com.aguardiantes.azarcafetero.auth_service.domain.exception.InvalidGoogleTokenException;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedConstruction;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * Pruebas adicionales de GoogleAuthAdapter que incrementan cobertura en la
 * rama del token válido (verifyToken con token no-nulo y no-blank).
 */
@ExtendWith(MockitoExtension.class)
class GoogleAuthAdapterExtendedTest {

    // ── Casos sin depender del verifier real ─────────────────────────────────

    @Test
    void verifyToken_withNullToken_throwsGoogleAuthorizationDeniedException() {
        GoogleAuthAdapter adapter = new GoogleAuthAdapter("client-id");
        assertThrows(GoogleAuthorizationDeniedException.class,
                () -> adapter.verifyToken(null));
    }

    @Test
    void verifyToken_withBlankToken_throwsGoogleAuthorizationDeniedException() {
        GoogleAuthAdapter adapter = new GoogleAuthAdapter("client-id");
        assertThrows(GoogleAuthorizationDeniedException.class,
                () -> adapter.verifyToken("   "));
    }

    @Test
    void verifyToken_withEmptyToken_throwsGoogleAuthorizationDeniedException() {
        GoogleAuthAdapter adapter = new GoogleAuthAdapter("client-id");
        assertThrows(GoogleAuthorizationDeniedException.class,
                () -> adapter.verifyToken(""));
    }

    /**
     * Verifica que un token con formato incorrecto (no-JWT) provoca que
     * el adaptador envuelva la excepción de bajo nivel en RuntimeException.
     */
    @Test
    void verifyToken_withMalformedToken_wrapsExceptionAsRuntimeException() {
        GoogleAuthAdapter adapter = new GoogleAuthAdapter("client-id");

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> adapter.verifyToken("not.a.valid.jwt.token"));

        assertTrue(ex.getMessage().contains("Error al contactar Google"));
    }

    /**
     * Verifica que InvalidGoogleTokenException no se re-envuelve:
     * se propaga directamente. No podemos obtener un idToken == null del
     * verifier sin acceso a Google, así que lo simulamos con MockedConstruction.
     */
    @Test
    void verifyToken_whenVerifierReturnsNull_throwsInvalidGoogleTokenException() {
        try (MockedConstruction<GoogleIdTokenVerifier.Builder> builderMock =
                     mockConstruction(GoogleIdTokenVerifier.Builder.class, (mock, context) -> {
                         GoogleIdTokenVerifier verifier = mock(GoogleIdTokenVerifier.class);
                         try {
                             when(verifier.verify(anyString())).thenReturn(null);
                         } catch (Exception e) {
                             throw new RuntimeException(e);
                         }

                         when(mock.setAudience(any())).thenReturn(mock);
                         when(mock.build()).thenReturn(verifier);
                     })) {

            GoogleAuthAdapter adapter = new GoogleAuthAdapter("client-id");

            assertThrows(InvalidGoogleTokenException.class,
                    () -> adapter.verifyToken("some-token-string"));
        }
    }

    /**
     * Verifica que un token válido devuelve GoogleUserData correctamente poblado.
     */
    @Test
    void verifyToken_whenVerifierReturnsValidToken_returnsGoogleUserData() throws Exception {
        GoogleIdToken.Payload payload = mock(GoogleIdToken.Payload.class);
        when(payload.getSubject()).thenReturn("google-sub-123");
        when(payload.getEmail()).thenReturn("user@gmail.com");
        when(payload.get("name")).thenReturn("Test User");
        when(payload.get("picture")).thenReturn("https://photo.url");

        GoogleIdToken idToken = mock(GoogleIdToken.class);
        when(idToken.getPayload()).thenReturn(payload);

        try (MockedConstruction<GoogleIdTokenVerifier.Builder> builderMock =
                     mockConstruction(GoogleIdTokenVerifier.Builder.class, (mock, context) -> {
                         GoogleIdTokenVerifier verifier = mock(GoogleIdTokenVerifier.class);
                         when(verifier.verify(anyString())).thenReturn(idToken);

                         when(mock.setAudience(any())).thenReturn(mock);
                         when(mock.build()).thenReturn(verifier);
                     })) {

            GoogleAuthAdapter adapter = new GoogleAuthAdapter("client-id");
            GoogleUserData result = adapter.verifyToken("valid-token");

            assertNotNull(result);
            assertEquals("google-sub-123", result.getGoogleId());
            assertEquals("user@gmail.com", result.getEmail());
            assertEquals("Test User", result.getName());
            assertEquals("https://photo.url", result.getAvatarUrl());
        }
    }
}
