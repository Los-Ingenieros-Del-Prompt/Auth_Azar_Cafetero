package com.aguardiantes.azarcafetero.auth_service.domain.model;

import com.aguardiantes.azarcafetero.auth_service.domain.model.value.Email;
import com.aguardiantes.azarcafetero.auth_service.domain.model.value.GoogleId;
import com.aguardiantes.azarcafetero.auth_service.domain.model.value.UserId;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void shouldRegisterUserWithGoogle() {
        GoogleId googleId = new GoogleId("google-123");
        Email email = new Email("test@mail.com");

        User user = User.registerWithGoogle(
                googleId,
                email,
                "Karol",
                "avatar.png"
        );

        assertNotNull(user.getId());
        assertEquals(googleId, user.getGoogleId());
        assertEquals(email, user.getEmail());
        assertEquals("Karol", user.getName());
        assertEquals("avatar.png", user.getAvatarUrl());

        assertNotNull(user.getCreatedAt());
        assertNotNull(user.getLastLoginAt());
        assertEquals(user.getCreatedAt(), user.getLastLoginAt());
    }

    @Test
    void shouldReconstituteUser() {
        UserId id = UserId.generate();
        GoogleId googleId = new GoogleId("google-999");
        Email email = new Email("restore@mail.com");

        Instant createdAt = Instant.now().minusSeconds(3600);
        Instant lastLogin = Instant.now().minusSeconds(60);

        User user = User.reconstitute(
                id,
                googleId,
                email,
                "Ana",
                "img.png",
                createdAt,
                lastLogin
        );

        assertEquals(id, user.getId());
        assertEquals(googleId, user.getGoogleId());
        assertEquals(email, user.getEmail());
        assertEquals("Ana", user.getName());
        assertEquals("img.png", user.getAvatarUrl());
        assertEquals(createdAt, user.getCreatedAt());
        assertEquals(lastLogin, user.getLastLoginAt());
    }

    @Test
    void shouldUpdateProfile() {
        User user = User.registerWithGoogle(
                new GoogleId("g-1"),
                new Email("a@mail.com"),
                "Old Name",
                "old.png"
        );

        user.updateProfile("New Name", "new.png");

        assertEquals("New Name", user.getName());
        assertEquals("new.png", user.getAvatarUrl());
    }

    @Test
    void shouldRecordLogin() throws InterruptedException {
        User user = User.registerWithGoogle(
                new GoogleId("g-2"),
                new Email("b@mail.com"),
                "User",
                "img.png"
        );

        Instant before = user.getLastLoginAt();

        Thread.sleep(5); // asegura cambio de timestamp

        user.recordLogin();

        assertTrue(user.getLastLoginAt().isAfter(before));
    }
}