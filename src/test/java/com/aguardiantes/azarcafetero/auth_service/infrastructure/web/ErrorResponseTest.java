package com.aguardiantes.azarcafetero.auth_service.infrastructure.web;


import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

class ErrorResponseTest {

    @Test
    void of_shouldCreateErrorResponseWithCorrectFields() {
        Instant before = Instant.now();

        ErrorResponse response = ErrorResponse.of(404, "Not Found", "Recurso no encontrado");

        Instant after = Instant.now();

        assertEquals(404, response.status());
        assertEquals("Not Found", response.error());
        assertEquals("Recurso no encontrado", response.message());
        assertNotNull(response.timestamp());
        assertFalse(response.timestamp().isBefore(before));
        assertFalse(response.timestamp().isAfter(after));
    }

    @Test
    void constructor_shouldCreateErrorResponseWithAllFields() {
        Instant timestamp = Instant.parse("2024-01-01T00:00:00Z");

        ErrorResponse response = new ErrorResponse(500, "Internal Server Error", "Error interno", timestamp);

        assertEquals(500, response.status());
        assertEquals("Internal Server Error", response.error());
        assertEquals("Error interno", response.message());
        assertEquals(timestamp, response.timestamp());
    }

    @Test
    void of_shouldGenerateDifferentTimestampsForDifferentCalls() throws InterruptedException {
        ErrorResponse first = ErrorResponse.of(400, "Bad Request", "Error 1");
        Thread.sleep(10);
        ErrorResponse second = ErrorResponse.of(400, "Bad Request", "Error 2");

        assertFalse(first.timestamp().isAfter(second.timestamp()));
    }
}