package com.aguardiantes.azarcafetero.auth_service.application.service;

import com.aguardiantes.azarcafetero.auth_service.application.dto.AuthenticationResponse;
import com.aguardiantes.azarcafetero.auth_service.application.dto.GoogleUserData;
import com.aguardiantes.azarcafetero.auth_service.application.port.out.GoogleAuthPort;
import com.aguardiantes.azarcafetero.auth_service.application.port.out.TokenGeneratorPort;
import com.aguardiantes.azarcafetero.auth_service.application.port.out.UserRepositoryPort;
import com.aguardiantes.azarcafetero.auth_service.domain.model.User;
import com.aguardiantes.azarcafetero.auth_service.domain.model.value.Email;
import com.aguardiantes.azarcafetero.auth_service.domain.model.value.GoogleId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthenticateWithGoogleServiceTest {

    @Mock
    private GoogleAuthPort googleAuthPort;

    @Mock
    private UserRepositoryPort userRepository;

    @Mock
    private TokenGeneratorPort tokenGeneratorPort;

    @InjectMocks
    private AuthenticateWithGoogleService service;

    private final GoogleUserData googleUserData = new GoogleUserData(
            "google-123",
            "test@gmail.com",
            "Test User",
            "https://avatar.url/photo.jpg"
    );

    @Test
    void authenticate_shouldRegisterNewUserAndReturnResponse() {
        // Arrange
        when(googleAuthPort.verifyToken("valid-token")).thenReturn(googleUserData);
        when(userRepository.findByGoogleId(GoogleId.of("google-123"))).thenReturn(Optional.empty());
        when(tokenGeneratorPort.generateToken(any(User.class))).thenReturn("jwt-token");

        // Act
        AuthenticationResponse response = service.authenticate("valid-token");

        // Assert
        assertEquals("jwt-token", response.token());
        assertEquals("Test User", response.name());
        assertEquals("https://avatar.url/photo.jpg", response.avatarUrl());
        assertTrue(response.isNewUser());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void authenticate_shouldUpdateExistingUserAndReturnResponse() {
        // Arrange
        User existingUser = User.registerWithGoogle(
                GoogleId.of("google-123"),
                Email.of("test@gmail.com"),
                "Old Name",
                "https://old-avatar.url"
        );

        when(googleAuthPort.verifyToken("valid-token")).thenReturn(googleUserData);
        when(userRepository.findByGoogleId(GoogleId.of("google-123"))).thenReturn(Optional.of(existingUser));
        when(tokenGeneratorPort.generateToken(existingUser)).thenReturn("jwt-token");

        // Act
        AuthenticationResponse response = service.authenticate("valid-token");

        // Assert
        assertEquals("jwt-token", response.token());
        assertEquals("Test User", response.name());
        assertEquals("https://avatar.url/photo.jpg", response.avatarUrl());
        assertFalse(response.isNewUser());
        verify(userRepository, times(1)).save(existingUser);
    }

    @Test
    void authenticate_shouldPropagateExceptionWhenTokenIsInvalid() {
        // Arrange
        when(googleAuthPort.verifyToken("invalid-token")).thenThrow(new RuntimeException("Token inválido"));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> service.authenticate("invalid-token"));
        verify(userRepository, never()).save(any());
    }

    @Test
    void authenticate_shouldCallVerifyTokenWithCorrectToken() {
        // Arrange
        when(googleAuthPort.verifyToken("valid-token")).thenReturn(googleUserData);
        when(userRepository.findByGoogleId(any())).thenReturn(Optional.empty());
        when(tokenGeneratorPort.generateToken(any())).thenReturn("jwt-token");

        // Act
        service.authenticate("valid-token");

        // Assert
        verify(googleAuthPort, times(1)).verifyToken("valid-token");
    }
}
