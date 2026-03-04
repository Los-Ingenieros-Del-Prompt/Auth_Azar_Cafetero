package com.aguardiantes.azarcafetero.auth_service.application.service;

import com.aguardiantes.azarcafetero.auth_service.application.dto.AuthenticationResponse;
import com.aguardiantes.azarcafetero.auth_service.domain.model.GoogleUserData;
import com.aguardiantes.azarcafetero.auth_service.domain.model.User;
import com.aguardiantes.azarcafetero.auth_service.domain.model.value.Email;
import com.aguardiantes.azarcafetero.auth_service.domain.model.value.GoogleId;
import com.aguardiantes.azarcafetero.auth_service.domain.port.in.AuthenticateWithGoogleUseCase;
import com.aguardiantes.azarcafetero.auth_service.domain.port.out.GoogleAuthPort;
import com.aguardiantes.azarcafetero.auth_service.domain.port.out.TokenGeneratorPort;
import com.aguardiantes.azarcafetero.auth_service.domain.port.out.UserRepository;

public class AuthenticateWithGoogleService implements AuthenticateWithGoogleUseCase {

    private final GoogleAuthPort googleAuthPort;
    private final UserRepository userRepository;
    private final TokenGeneratorPort tokenGeneratorPort;

    public AuthenticateWithGoogleService(
            GoogleAuthPort googleAuthPort,
            UserRepository userRepository,
            TokenGeneratorPort tokenGeneratorPort
    ) {
        this.googleAuthPort = googleAuthPort;
        this.userRepository = userRepository;
        this.tokenGeneratorPort = tokenGeneratorPort;
    }

    @Override
    public AuthenticationResponse authenticate(String idToken) {

        // 1️⃣ Verificar token con Google
        GoogleUserData googleUser = googleAuthPort.verifyToken(idToken);

        GoogleId googleId = GoogleId.of(googleUser.getGoogleId());

        // 2️⃣ Buscar usuario
        var optionalUser = userRepository.findByGoogleId(googleId);

        boolean isNewUser;
        User user;

        if (optionalUser.isEmpty()) {
            // 3️⃣ Crear usuario nuevo
            user = User.registerWithGoogle(
                    googleId,
                    Email.of(googleUser.getEmail()),
                    googleUser.getName(),
                    googleUser.getAvatarUrl()
            );

            userRepository.save(user);
            isNewUser = true;

        } else {
            // 4️⃣ Usuario existente
            user = optionalUser.get();
            user.updateProfile(
                    googleUser.getName(),
                    googleUser.getAvatarUrl()
            );
            user.recordLogin();

            userRepository.save(user);
            isNewUser = false;
        }

        // 5️⃣ Generar JWT
        String token = tokenGeneratorPort.generateToken(user);

        // 6️⃣ Retornar DTO
        return new AuthenticationResponse(
                token,
                user.getName(),
                user.getAvatarUrl(),
                isNewUser
        );
    }
}