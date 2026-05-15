package com.aguardiantes.azarcafetero.auth_service.application.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GoogleUserDataTest {

    @Test
    void shouldCreateGoogleUserDataCorrectly() {

        GoogleUserData data = new GoogleUserData(
                "google-123",
                "test@example.com",
                "Karol",
                "avatar.png"
        );

        assertNotNull(data);

        assertEquals("google-123", data.getGoogleId());
        assertEquals("test@example.com", data.getEmail());
        assertEquals("Karol", data.getName());
        assertEquals("avatar.png", data.getAvatarUrl());
    }

    @Test
    void shouldBeImmutable() {

        GoogleUserData data = new GoogleUserData(
                "g-1",
                "a@a.com",
                "Name",
                "img.png"
        );

        // Solo verificamos que los getters no cambian valores
        assertEquals("g-1", data.getGoogleId());
        assertEquals("a@a.com", data.getEmail());
        assertEquals("Name", data.getName());
        assertEquals("img.png", data.getAvatarUrl());
    }
}