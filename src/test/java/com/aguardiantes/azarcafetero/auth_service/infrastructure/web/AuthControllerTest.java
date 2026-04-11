package com.aguardiantes.azarcafetero.auth_service.infrastructure.web;

import com.aguardiantes.azarcafetero.auth_service.application.dto.AuthenticationResponse;
import com.aguardiantes.azarcafetero.auth_service.application.port.in.AuthenticateWithGoogleUseCase;
import com.aguardiantes.azarcafetero.auth_service.infrastructure.web.dto.GoogleAuthRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock
    private AuthenticateWithGoogleUseCase useCase;

    @InjectMocks
    private AuthController authController;

    @Test
    void googleAuth_shouldReturnOkWithAuthenticationResponse() {
        String idToken = "fake-google-token";
        GoogleAuthRequest request = new GoogleAuthRequest(idToken);

        AuthenticationResponse mockResponse = new AuthenticationResponse(
                "jwt-token",
                "test@example.com",     
                "Test User",
                "https://avatar.url/test.png",
                true
        );

        when(useCase.authenticate(idToken)).thenReturn(mockResponse);

        ResponseEntity<AuthenticationResponse> result = authController.googleAuth(request);

        assertEquals(200, result.getStatusCode().value());
        assertEquals(mockResponse, result.getBody());
        verify(useCase, times(1)).authenticate(idToken);
    }

    @Test
    void googleAuth_shouldCallUseCaseWithCorrectToken() {
        String idToken = "another-fake-token";
        GoogleAuthRequest request = new GoogleAuthRequest(idToken);

        AuthenticationResponse mockResponse = new AuthenticationResponse(
                "jwt-token-2",
                "another@example.com", 
                "Another User",
                "https://avatar.url/another.png",
                false
        );

        when(useCase.authenticate(idToken)).thenReturn(mockResponse);

        authController.googleAuth(request);

        verify(useCase).authenticate(idToken);
    }

    @Test
    void googleAuth_shouldPropagateExceptionWhenUseCaseFails() {
        String idToken = "invalid-token";
        GoogleAuthRequest request = new GoogleAuthRequest(idToken);

        when(useCase.authenticate(idToken)).thenThrow(new RuntimeException("Invalid token"));

        assertThrows(RuntimeException.class, () -> authController.googleAuth(request));
    }
}
