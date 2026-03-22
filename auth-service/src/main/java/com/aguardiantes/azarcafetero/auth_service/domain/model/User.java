package com.aguardiantes.azarcafetero.auth_service.domain.model;

import com.aguardiantes.azarcafetero.auth_service.domain.model.value.Email;
import com.aguardiantes.azarcafetero.auth_service.domain.model.value.GoogleId;
import com.aguardiantes.azarcafetero.auth_service.domain.model.value.UserId;
import lombok.Getter;

import java.time.Instant;

@Getter
public class User {

    private final UserId id;
    private final GoogleId googleId;
    private final Email email;
    private String name;
    private String avatarUrl;
    private final Instant createdAt;
    private Instant lastLoginAt;

    private User(
            UserId id,
            GoogleId googleId,
            Email email,
            String name,
            String avatarUrl,
            Instant createdAt,
            Instant lastLoginAt
    ) {
        this.id = id;
        this.googleId = googleId;
        this.email = email;
        this.name = name;
        this.avatarUrl = avatarUrl;
        this.createdAt = createdAt;
        this.lastLoginAt = lastLoginAt;
    }

    public static User registerWithGoogle(
            GoogleId googleId,
            Email email,
            String name,
            String avatarUrl
    ) {
        Instant now = Instant.now();
        return new User(
                UserId.generate(),
                googleId,
                email,
                name,
                avatarUrl,
                now,
                now
        );
    }

    public static User reconstitute(
            UserId id,
            GoogleId googleId,
            Email email,
            String name,
            String avatarUrl,
            Instant createdAt,
            Instant lastLoginAt
    ) {
        return new User(id, googleId, email, name, avatarUrl, createdAt, lastLoginAt);
    }

    public void updateProfile(String name, String avatarUrl) {
        this.name = name;
        this.avatarUrl = avatarUrl;
    }

    public void recordLogin() {
        this.lastLoginAt = Instant.now();
    }
}