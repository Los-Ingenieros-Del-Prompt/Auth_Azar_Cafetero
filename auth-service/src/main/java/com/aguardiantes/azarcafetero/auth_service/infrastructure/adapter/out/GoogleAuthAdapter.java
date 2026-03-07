package com.aguardiantes.azarcafetero.auth_service.infrastructure.adapter.out;

import com.aguardiantes.azarcafetero.auth_service.application.dto.GoogleUserData;
import com.aguardiantes.azarcafetero.auth_service.domain.exception.GoogleAuthorizationDeniedException;
import com.aguardiantes.azarcafetero.auth_service.domain.exception.InvalidGoogleTokenException;
import com.aguardiantes.azarcafetero.auth_service.application.port.out.GoogleAuthPort;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory; // ✅ GsonFactory (JacksonFactory está deprecado)
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
public class GoogleAuthAdapter implements GoogleAuthPort {


    private final String clientId;

    public GoogleAuthAdapter(@Value("${google.client-id}") String clientId) {
        this.clientId = clientId;
    }

    @Override
    public GoogleUserData verifyToken(String idTokenString) {


        if (idTokenString == null || idTokenString.isBlank()) {
            throw new GoogleAuthorizationDeniedException("El usuario canceló la autorización de Google");
        }

        try {
            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(
                    new NetHttpTransport(),
                    GsonFactory.getDefaultInstance())
                    .setAudience(Collections.singletonList(clientId))
                    .build();

            GoogleIdToken idToken = verifier.verify(idTokenString);


            if (idToken == null) {
                throw new InvalidGoogleTokenException("Token de Google inválido o expirado");
            }

            GoogleIdToken.Payload payload = idToken.getPayload();

            return new GoogleUserData(
                    payload.getSubject(),
                    payload.getEmail(),
                    (String) payload.get("name"),
                    (String) payload.get("picture")
            );

        } catch (InvalidGoogleTokenException | GoogleAuthorizationDeniedException e) {
            throw e;
        } catch (Exception e) {

            throw new RuntimeException("Error al contactar Google: " + e.getMessage(), e);
        }
    }
}