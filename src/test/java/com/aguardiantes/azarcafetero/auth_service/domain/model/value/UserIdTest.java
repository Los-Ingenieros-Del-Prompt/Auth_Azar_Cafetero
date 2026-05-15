package com.aguardiantes.azarcafetero.auth_service.domain.model.value;

import org.junit.jupiter.api.Test;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

class UserIdTest {

    @Test
    void generate_shouldCreateNonNullUserId() {
        UserId id = UserId.generate();
        assertNotNull(id);
        assertNotNull(id.value());
    }

    @Test
    void generate_shouldCreateUniqueIds() {
        UserId first = UserId.generate();
        UserId second = UserId.generate();
        assertNotEquals(first, second);
    }

    @Test
    void of_shouldCreateUserIdFromValidUuidString() {
        String uuid = "550e8400-e29b-41d4-a716-446655440000";
        UserId id = UserId.of(uuid);
        assertEquals(UUID.fromString(uuid), id.value());
    }

    @Test
    void of_shouldThrowWhenStringIsNotValidUuid() {
        assertThrows(IllegalArgumentException.class, () -> UserId.of("not-a-uuid"));
    }

    @Test
    void constructor_shouldThrowWhenUuidIsNull() {
        assertThrows(IllegalArgumentException.class, () -> new UserId(null));
    }

    @Test
    void toString_shouldReturnUuidStringRepresentation() {
        String uuid = "550e8400-e29b-41d4-a716-446655440000";
        UserId id = UserId.of(uuid);
        assertEquals(uuid, id.toString());
    }

    @Test
    void twoUserIdsWithSameUuid_shouldBeEqual() {
        String uuid = "550e8400-e29b-41d4-a716-446655440000";
        assertEquals(UserId.of(uuid), UserId.of(uuid));
    }
}