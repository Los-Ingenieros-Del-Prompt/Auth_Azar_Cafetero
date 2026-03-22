package com.aguardiantes.azarcafetero.auth_service.infrastructure.web;

import com.aguardiantes.azarcafetero.auth_service.application.dto.AuthenticationResponse;
import com.aguardiantes.azarcafetero.auth_service.application.port.in.AuthenticateWithGoogleUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticateWithGoogleUseCase useCase;

    public AuthController(AuthenticateWithGoogleUseCase useCase) {
        this.useCase = useCase;
    }

    @PostMapping("/google")
    public ResponseEntity<AuthenticationResponse> googleAuth(
            @RequestBody GoogleAuthRequest request) {

        AuthenticationResponse response = useCase.authenticate(request.idToken());
        return ResponseEntity.ok(response);
    }
}