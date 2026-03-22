package com.aguardiantes.azarcafetero.auth_service.domain.model.value;



public record Email(String value) {

    public Email {
        if (value == null) {
            throw new IllegalArgumentException("El email no puede ser null");
        }

        value = value.trim().toLowerCase();

        if (value.isBlank()) {
            throw new IllegalArgumentException("El email no puede estar vacío");
        }

        if (!value.matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")) {
            throw new IllegalArgumentException("Formato inválido: " + value);
        }
    }

    public static Email of(String value) {
        return new Email(value);
    }

    @Override
    public String toString() {
        return value;
    }
}