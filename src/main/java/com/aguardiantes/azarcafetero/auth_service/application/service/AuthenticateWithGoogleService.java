package com.aguardiantes.azarcafetero.auth_service.application.service;

import com.aguardiantes.azarcafetero.auth_service.application.dto.AuthenticationResponse;
import com.aguardiantes.azarcafetero.auth_service.application.port.in.AuthenticateWithGoogleUseCase;
import com.aguardiantes.azarcafetero.auth_service.application.dto.GoogleUserData;
import com.aguardiantes.azarcafetero.auth_service.application.port.out.TokenGeneratorPort;
import com.aguardiantes.azarcafetero.auth_service.application.port.out.UserRepositoryPort;
import com.aguardiantes.azarcafetero.auth_service.domain.model.User;
import com.aguardiantes.azarcafetero.auth_service.domain.model.value.Email;
import com.aguardiantes.azarcafetero.auth_service.domain.model.value.GoogleId;
import com.aguardiantes.azarcafetero.auth_service.application.port.out.GoogleAuthPort;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthenticateWithGoogleService implements AuthenticateWithGoogleUseCase {

    private final GoogleAuthPort googleAuthPort;
    private final UserRepositoryPort userRepository;
    private final TokenGeneratorPort tokenGeneratorPort;

    public AuthenticateWithGoogleService(
            GoogleAuthPort googleAuthPort,
            UserRepositoryPort userRepository,
            TokenGeneratorPort tokenGeneratorPort
    ) {
        this.googleAuthPort = googleAuthPort;
        this.userRepository = userRepository;
        this.tokenGeneratorPort = tokenGeneratorPort;
    }

    @Override

    @Transactional
    public AuthenticationResponse authenticate(String idToken) {


        GoogleUserData googleUser = googleAuthPort.verifyToken(idToken);
        GoogleId googleId = GoogleId.of(googleUser.getGoogleId());


        var optionalUser = userRepository.findByGoogleId(googleId);

        boolean isNewUser;
        User user;

        if (optionalUser.isEmpty()) {

            user = User.registerWithGoogle(
                    googleId,
                    Email.of(googleUser.getEmail()),
                    googleUser.getName(),
                    googleUser.getAvatarUrl()
            );
            userRepository.save(user);
            isNewUser = true;

        } else {

            user = optionalUser.get();
            user.updateProfile(googleUser.getName(), googleUser.getAvatarUrl());
            user.recordLogin();
            userRepository.save(user);
            isNewUser = false;
        }


        String token = tokenGeneratorPort.generateToken(user);


        return new AuthenticationResponse(
                token,
                user.getName(),
                user.getAvatarUrl(),
                isNewUser
        );
    }
}