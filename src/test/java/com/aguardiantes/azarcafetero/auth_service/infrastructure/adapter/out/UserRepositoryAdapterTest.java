package com.aguardiantes.azarcafetero.auth_service.infrastructure.adapter.out;

import com.aguardiantes.azarcafetero.auth_service.domain.model.User;
import com.aguardiantes.azarcafetero.auth_service.domain.model.value.Email;
import com.aguardiantes.azarcafetero.auth_service.domain.model.value.GoogleId;
import com.aguardiantes.azarcafetero.auth_service.infrastructure.persistence.JpaUserRepository;
import com.aguardiantes.azarcafetero.auth_service.infrastructure.persistence.UserJpaEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserRepositoryAdapterTest {

    @Mock
    private JpaUserRepository repository;

    @InjectMocks
    private UserRepositoryAdapter adapter;

    @Test
void shouldFindUserByGoogleId() {

    GoogleId googleId = new GoogleId("google-123");

    UserJpaEntity entity = new UserJpaEntity();

    entity.setId(java.util.UUID.randomUUID());
    entity.setGoogleId("google-123");
    entity.setEmail("test@example.com");
    entity.setName("Karol");
    entity.setAvatarUrl("avatar.png");
    entity.setCreatedAt(Instant.now());
    entity.setLastLoginAt(Instant.now());

    when(repository.findByGoogleId("google-123"))
            .thenReturn(Optional.of(entity));

    Optional<User> result = adapter.findByGoogleId(googleId);

    assertTrue(result.isPresent());

    User user = result.get();

    assertEquals("google-123", user.getGoogleId().value());
    assertEquals("test@example.com", user.getEmail().value());
    assertEquals("Karol", user.getName());

    verify(repository).findByGoogleId("google-123");
}

    @Test
    void shouldReturnEmptyWhenUserNotFound() {

        GoogleId googleId = new GoogleId("not-found");

        when(repository.findByGoogleId("not-found"))
                .thenReturn(Optional.empty());

        Optional<User> result = adapter.findByGoogleId(googleId);

        assertTrue(result.isEmpty());

        verify(repository).findByGoogleId("not-found");
    }

    @Test
    void shouldSaveUser() {

        User user = User.registerWithGoogle(
                new GoogleId("google-999"),
                new Email("save@example.com"),
                "Save User",
                "save.png"
        );

        adapter.save(user);

        verify(repository, times(1)).save(any(UserJpaEntity.class));
    }

    @Test
    void shouldReturnSameUserAfterSave() {

        User user = User.registerWithGoogle(
                new GoogleId("google-777"),
                new Email("same@example.com"),
                "Same User",
                "same.png"
        );

        User savedUser = adapter.save(user);

        assertEquals(user, savedUser);

        verify(repository).save(any(UserJpaEntity.class));
    }
}