package com.aguardiantes.azarcafetero.auth_service.domain.model;


public class GoogleUserData {

    private final String googleId;
    private final String email;
    private final String name;
    private final String avatarUrl;

    public GoogleUserData(
            String googleId,
            String email,
            String name,
            String avatarUrl
    ) {
        this.googleId = googleId;
        this.email = email;
        this.name = name;
        this.avatarUrl = avatarUrl;
    }

    public String getGoogleId() {
        return googleId;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }
}