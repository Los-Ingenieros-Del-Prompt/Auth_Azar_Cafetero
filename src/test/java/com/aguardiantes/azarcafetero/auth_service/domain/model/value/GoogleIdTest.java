package com.aguardiantes.azarcafetero.auth_service.domain.model.value;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GoogleIdTest {

    @Test
    void of_shouldCreateGoogleIdWithCorrectValue() {
        GoogleId googleId = GoogleId.of("google-123");
        assertEquals("google-123", googleId.value());
    }

    @Test
    void of_shouldTrimWhitespace() {
        GoogleId googleId = GoogleId.of("  google-123  ");
        assertEquals("google-123", googleId.value());
    }

    @Test
    void of_shouldThrowWhenNull() {
        assertThrows(IllegalArgumentException.class, () -> GoogleId.of(null));
    }

    @Test
    void of_shouldThrowWhenBlank() {
        assertThrows(IllegalArgumentException.class, () -> GoogleId.of("   "));
    }

    @Test
    void toString_shouldReturnValue() {
        GoogleId googleId = GoogleId.of("google-123");
        assertEquals("google-123", googleId.toString());
    }

    @Test
    void twoGoogleIdsWithSameValue_shouldBeEqual() {
        GoogleId first = GoogleId.of("google-123");
        GoogleId second = GoogleId.of("google-123");
        assertEquals(first, second);
    }
}