package com.aguardiantes.azarcafetero.auth_service.domain.model.value;


import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EmailTest {

    @Test
    void of_shouldCreateEmailWithCorrectValue() {
        Email email = Email.of("test@gmail.com");
        assertEquals("test@gmail.com", email.value());
    }

    @Test
    void of_shouldTrimAndLowercaseEmail() {
        Email email = Email.of("  TEST@GMAIL.COM  ");
        assertEquals("test@gmail.com", email.value());
    }

    @Test
    void of_shouldThrowWhenNull() {
        assertThrows(IllegalArgumentException.class, () -> Email.of(null));
    }

    @Test
    void of_shouldThrowWhenBlank() {
        assertThrows(IllegalArgumentException.class, () -> Email.of("   "));
    }

    @Test
    void of_shouldThrowWhenFormatIsInvalid() {
        assertThrows(IllegalArgumentException.class, () -> Email.of("not-an-email"));
    }

    @Test
    void of_shouldThrowWhenMissingDomain() {
        assertThrows(IllegalArgumentException.class, () -> Email.of("test@"));
    }

    @Test
    void of_shouldThrowWhenMissingAtSign() {
        assertThrows(IllegalArgumentException.class, () -> Email.of("testgmail.com"));
    }

    @Test
    void toString_shouldReturnValue() {
        Email email = Email.of("test@gmail.com");
        assertEquals("test@gmail.com", email.toString());
    }

    @Test
    void twoEmailsWithSameValue_shouldBeEqual() {
        Email first = Email.of("test@gmail.com");
        Email second = Email.of("TEST@GMAIL.COM");
        assertEquals(first, second);
    }
}