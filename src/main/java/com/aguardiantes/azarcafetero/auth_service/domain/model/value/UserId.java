package com.aguardiantes.azarcafetero.auth_service.domain.model.value;


import java.util.UUID;

public record UserId(UUID value) {


    public UserId {
        if (value == null) {
            throw new IllegalArgumentException("UserId no puede ser nulo");
        }
    }

    public static UserId generate() {
        return new UserId(UUID.randomUUID());
    }

    public static UserId of(String value) {
        try {
            return new UserId(UUID.fromString(value));
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("UserId inválido: " + value);
        }
    }

    @Override
    public String toString() {
        return value.toString();
    }
}