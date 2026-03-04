package com.aguardiantes.azarcafetero.auth_service.domain.model.value;

public record GoogleId(String value) {

    public GoogleId {
        if (value == null) {
            throw new IllegalArgumentException("GoogleId no puede ser null");
        }

        value = value.trim();

        if (value.isBlank()) {
            throw new IllegalArgumentException("GoogleId no puede estar vacío");
        }
    }

    public static GoogleId of(String value) {
        return new GoogleId(value);
    }

    @Override
    public String toString() {
        return value;
    }
}