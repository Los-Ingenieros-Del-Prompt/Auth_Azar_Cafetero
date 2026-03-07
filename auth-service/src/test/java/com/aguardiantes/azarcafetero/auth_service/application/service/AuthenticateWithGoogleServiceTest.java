package com.aguardiantes.azarcafetero.auth_service.application.service;

import com.aguardiantes.azarcafetero.auth_service.application.dto.AuthenticationResponse;
import com.aguardiantes.azarcafetero.auth_service.application.dto.GoogleUserData;
import com.aguardiantes.azarcafetero.auth_service.application.port.out.GoogleAuthPort;
import com.aguardiantes.azarcafetero.auth_service.application.port.out.TokenGeneratorPort;
import com.aguardiantes.azarcafetero.auth_service.application.port.out.UserRepositoryPort;
import com.aguardiantes.azarcafetero.auth_service.domain.exception.GoogleAuthorizationDeniedException;

import com.aguardiantes.azarcafetero.auth_service.domain.model.User;
import com.aguardiantes.azarcafetero.auth_service.domain.model.value.Email;
import com.aguardiantes.azarcafetero.auth_service.domain.model.value.GoogleId;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthenticateWithGoogleServiceTest {

    @Mock private GoogleAuthPort googleAuthPort;
    @Mock private UserRepositoryPort userRepository;
    @Mock private TokenGeneratorPort tokenGeneratorPort;

    private AuthenticateWithGoogleService service;


    private static final String VALID_ID_TOKEN  = "valid-google-token";
    private static final String GOOGLE_ID       = "google-123";
    private static final String EMAIL           = "karolestefany29@gmail.com";
    private static final String NAME            = "Karol Estupiñan";
    private static final String AVATAR          = "https://avatar.url/foto.jpg";
    private static final String JWT             = "jwt.token.generado";

    private GoogleUserData googleUserData;

    @BeforeEach
    void setUp() {
        service = new AuthenticateWithGoogleService(googleAuthPort, userRepository, tokenGeneratorPort);
        googleUserData = new GoogleUserData(GOOGLE_ID, EMAIL, NAME, AVATAR);
    }



    @Test
    @DisplayName("Registro primera vez: crea usuario, devuelve isNewUser=true y token")
    void shouldRegisterNewUserOnFirstLogin() {
        when(googleAuthPort.verifyToken(VALID_ID_TOKEN)).thenReturn(googleUserData);
        when(userRepository.findByGoogleId(GoogleId.of(GOOGLE_ID))).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));
        when(tokenGeneratorPort.generateToken(any(User.class))).thenReturn(JWT);

        AuthenticationResponse response = service.authenticate(VALID_ID_TOKEN);

        assertThat(response.isNewUser()).isTrue();
        assertThat(response.token()).isEqualTo(JWT);
        assertThat(response.name()).isEqualTo(NAME);
        assertThat(response.avatarUrl()).isEqualTo(AVATAR);

        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("Registro primera vez: tiempo de respuesta menor a 5 segundos")
    void registrationShouldCompleteWithin5Seconds() {
        when(googleAuthPort.verifyToken(VALID_ID_TOKEN)).thenReturn(googleUserData);
        when(userRepository.findByGoogleId(any())).thenReturn(Optional.empty());
        when(userRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));
        when(tokenGeneratorPort.generateToken(any())).thenReturn(JWT);

        long start = System.currentTimeMillis();
        service.authenticate(VALID_ID_TOKEN);
        long elapsed = System.currentTimeMillis() - start;

        assertThat(elapsed)
                .as("El registro debe completarse en menos de 5000ms, tardó: %dms", elapsed)
                .isLessThan(5000L);
    }


    @Test
    @DisplayName("Login existente: no crea usuario, devuelve isNewUser=false y actualiza perfil")
    void shouldLoginExistingUser() {
        User existingUser = User.registerWithGoogle(
                GoogleId.of(GOOGLE_ID),
                Email.of(EMAIL),
                NAME,
                AVATAR
        );

        when(googleAuthPort.verifyToken(VALID_ID_TOKEN)).thenReturn(googleUserData);
        when(userRepository.findByGoogleId(GoogleId.of(GOOGLE_ID))).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));
        when(tokenGeneratorPort.generateToken(any(User.class))).thenReturn(JWT);

        AuthenticationResponse response = service.authenticate(VALID_ID_TOKEN);

        assertThat(response.isNewUser()).isFalse();
        assertThat(response.token()).isEqualTo(JWT);


        verify(userRepository, times(1)).save(existingUser);
    }

    @Test
    @DisplayName("Login existente: tiempo de respuesta menor a 3 segundos")
    void loginShouldCompleteWithin3Seconds() {
        User existingUser = User.registerWithGoogle(
                GoogleId.of(GOOGLE_ID),
                Email.of(EMAIL),
                NAME,
                AVATAR
        );

        when(googleAuthPort.verifyToken(VALID_ID_TOKEN)).thenReturn(googleUserData);
        when(userRepository.findByGoogleId(any())).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));
        when(tokenGeneratorPort.generateToken(any())).thenReturn(JWT);

        long start = System.currentTimeMillis();
        service.authenticate(VALID_ID_TOKEN);
        long elapsed = System.currentTimeMillis() - start;

        assertThat(elapsed)
                .as("El login debe completarse en menos de 3000ms, tardó: %dms", elapsed)
                .isLessThan(3000L);
    }



    @Test
    @DisplayName("Autorización denegada: lanza excepción y no guarda ningún usuario")
    void shouldNotSaveUserWhenAuthorizationDenied() {
        when(googleAuthPort.verifyToken(null))
                .thenThrow(new GoogleAuthorizationDeniedException("El usuario canceló la autorización"));

        assertThatThrownBy(() -> service.authenticate(null))
                .isInstanceOf(GoogleAuthorizationDeniedException.class)
                .hasMessageContaining("canceló");

        verify(userRepository, never()).save(any());
        verify(userRepository, never()).findByGoogleId(any());
        verify(tokenGeneratorPort, never()).generateToken(any());
    }

    @Test
    @DisplayName("Token inválido: lanza excepción y no guarda ningún usuario")
    void shouldNotSaveUserWhenTokenIsInvalid() {
        when(googleAuthPort.verifyToken("token-invalido"))
                .thenThrow(new RuntimeException("Token de Google inválido o expirado"));

        assertThatThrownBy(() -> service.authenticate("token-invalido"))
                .isInstanceOf(RuntimeException.class);

        verify(userRepository, never()).save(any());
    }
}