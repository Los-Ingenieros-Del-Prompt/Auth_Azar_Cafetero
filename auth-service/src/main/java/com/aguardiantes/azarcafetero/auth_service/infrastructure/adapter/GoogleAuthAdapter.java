package com.aguardiantes.azarcafetero.auth_service.infrastructure.adapter;

import com.aguardiantes.azarcafetero.auth_service.domain.model.GoogleUserData;
import com.aguardiantes.azarcafetero.auth_service.domain.port.out.GoogleAuthPort;
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
        try {
            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(
                    new NetHttpTransport(),
                    GsonFactory.getDefaultInstance())
                    .setAudience(Collections.singletonList(clientId))
                    .build();

            GoogleIdToken idToken = verifier.verify(idTokenString);

            if (idToken == null) {
                throw new RuntimeException("Token de Google inválido o expirado");
            }

            GoogleIdToken.Payload payload = idToken.getPayload();


            return new GoogleUserData(
                    payload.getSubject(),
                    payload.getEmail(),
                    (String) payload.get("name"),
                    (String) payload.get("picture")
            );

        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error al verificar token de Google", e);
        }
    }
}