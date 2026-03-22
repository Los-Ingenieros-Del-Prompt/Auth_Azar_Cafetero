package com.aguardiantes.azarcafetero.auth_service.infrastructure.web;

import com.aguardiantes.azarcafetero.auth_service.application.dto.AuthenticationResponse;
import com.aguardiantes.azarcafetero.auth_service.application.port.in.AuthenticateWithGoogleUseCase;
import com.aguardiantes.azarcafetero.auth_service.infrastructure.web.dto.GoogleAuthRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@Tag(name = "Authentication", description = "Authentication endpoints using Google OAuth")
public class AuthController {

    private final AuthenticateWithGoogleUseCase useCase;

    public AuthController(AuthenticateWithGoogleUseCase useCase) {
        this.useCase = useCase;
    }

    @Operation(
            summary = "Authenticate with Google",
            description = "Authenticates a user using the Google ID Token provided by the client"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Authentication successful"),
            @ApiResponse(responseCode = "401", description = "Invalid Google token"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/google")
    public ResponseEntity<AuthenticationResponse> googleAuth(
            @RequestBody GoogleAuthRequest request) {

        AuthenticationResponse response = useCase.authenticate(request.idToken());
        return ResponseEntity.ok(response);
    }
}