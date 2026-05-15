package com.aguardiantes.azarcafetero.auth_service.infrastructure.persistence;

import com.aguardiantes.azarcafetero.auth_service.domain.model.User;
import com.aguardiantes.azarcafetero.auth_service.domain.model.value.Email;
import com.aguardiantes.azarcafetero.auth_service.domain.model.value.GoogleId;
import com.aguardiantes.azarcafetero.auth_service.domain.model.value.UserId;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class UserMapperTest {

    @Test
    void shouldMapEntityToDomain() {

        UserJpaEntity entity = new UserJpaEntity();

        UUID id = UUID.randomUUID();

        entity.setId(id);
        entity.setGoogleId("google-123");
        entity.setEmail("test@example.com");
        entity.setName("Karol");
        entity.setAvatarUrl("avatar.png");
        entity.setCreatedAt(Instant.now());
        entity.setLastLoginAt(Instant.now());

        User user = UserMapper.toDomain(entity);

        assertNotNull(user);

        assertEquals("google-123", user.getGoogleId().value());
        assertEquals("test@example.com", user.getEmail().value());
        assertEquals("Karol", user.getName());
        assertEquals("avatar.png", user.getAvatarUrl());

        assertNotNull(user.getId());
        assertNotNull(user.getCreatedAt());
        assertNotNull(user.getLastLoginAt());
    }

    @Test
    void shouldMapDomainToEntity() {

        Instant now = Instant.now();
        UUID uuid = UUID.randomUUID();

        User user = User.reconstitute(
                UserId.of(uuid.toString()),
                GoogleId.of("google-456"),
                Email.of("mapper@example.com"),
                "User Mapper",
                "avatar2.png",
                now,
                now
        );

        UserJpaEntity entity = UserMapper.toEntity(user);

        assertNotNull(entity);

        assertEquals(uuid, entity.getId()); // UUID real
        assertEquals("google-456", entity.getGoogleId());
        assertEquals("mapper@example.com", entity.getEmail());
        assertEquals("User Mapper", entity.getName());
        assertEquals("avatar2.png", entity.getAvatarUrl());

        assertEquals(now, entity.getCreatedAt());
        assertEquals(now, entity.getLastLoginAt());
    }
}