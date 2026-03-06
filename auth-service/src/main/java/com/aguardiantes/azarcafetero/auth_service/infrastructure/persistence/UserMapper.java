package com.aguardiantes.azarcafetero.auth_service.infrastructure.persistence;

import com.aguardiantes.azarcafetero.auth_service.domain.model.User;
import com.aguardiantes.azarcafetero.auth_service.domain.model.value.Email;
import com.aguardiantes.azarcafetero.auth_service.domain.model.value.GoogleId;
import com.aguardiantes.azarcafetero.auth_service.domain.model.value.UserId;

public class UserMapper {

    private UserMapper() {} // Utilidad estática, no instanciar

    public static User toDomain(UserJpaEntity entity) {
        return User.reconstitute(
                UserId.of(entity.getId().toString()),
                GoogleId.of(entity.getGoogleId()),
                Email.of(entity.getEmail()),
                entity.getName(),
                entity.getAvatarUrl(),
                entity.getCreatedAt(),
                entity.getLastLoginAt()
        );
    }

    public static UserJpaEntity toEntity(User user) {
        UserJpaEntity entity = new UserJpaEntity();
        entity.setId(user.getId().value());
        entity.setGoogleId(user.getGoogleId().value());
        entity.setEmail(user.getEmail().value());
        entity.setName(user.getName());
        entity.setAvatarUrl(user.getAvatarUrl());
        entity.setCreatedAt(user.getCreatedAt());
        entity.setLastLoginAt(user.getLastLoginAt());
        return entity;
    }
}