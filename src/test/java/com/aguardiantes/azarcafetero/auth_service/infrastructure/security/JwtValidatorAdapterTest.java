package com.aguardiantes.azarcafetero.auth_service.infrastructure.security;

import com.aguardiantes.azarcafetero.auth_service.domain.model.User;
import com.aguardiantes.azarcafetero.auth_service.domain.model.value.Email;
import com.aguardiantes.azarcafetero.auth_service.domain.model.value.GoogleId;
import com.aguardiantes.azarcafetero.auth_service.infrastructure.adapter.out.JwtTokenAdapter;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JwtValidatorAdapterTest {

    private static final String SECRET =
            "mySuperSecretKeyThatHasAtLeastThirtyTwoCharacters";

    @Test
    void shouldValidateCorrectToken() {

        JwtTokenAdapter tokenAdapter =
                new JwtTokenAdapter(SECRET, 86400000);

        JwtValidatorAdapter validator =
                new JwtValidatorAdapter(SECRET);

        User user = User.registerWithGoogle(
                new GoogleId("google-123"),
                new Email("karol@example.com"),
                "Karol",
                "avatar.png"
        );

        String token = tokenAdapter.generateToken(user);

        boolean valid = validator.isValid(token);

        assertTrue(valid);
    }

    @Test
    void shouldReturnFalseForInvalidToken() {

        JwtValidatorAdapter validator =
                new JwtValidatorAdapter(SECRET);

        boolean valid = validator.isValid("invalid-token");

        assertFalse(valid);
    }

    @Test
    void shouldExtractUserIdFromToken() {

        JwtTokenAdapter tokenAdapter =
                new JwtTokenAdapter(SECRET, 86400000);

        JwtValidatorAdapter validator =
                new JwtValidatorAdapter(SECRET);

        User user = User.registerWithGoogle(
                new GoogleId("google-456"),
                new Email("user@example.com"),
                "User Test",
                "avatar.png"
        );

        String token = tokenAdapter.generateToken(user);

        String subject = validator.extractUserId(token);

        assertEquals("user@example.com", subject);
    }

    @Test
    void shouldReturnFalseWhenTokenIsNull() {

        JwtValidatorAdapter validator =
                new JwtValidatorAdapter(SECRET);

        boolean valid = validator.isValid(null);

        assertFalse(valid);
    }

    @Test
    void shouldReturnFalseWhenTokenIsEmpty() {

        JwtValidatorAdapter validator =
                new JwtValidatorAdapter(SECRET);

        boolean valid = validator.isValid("");

        assertFalse(valid);
    }
}