package com.aguardiantes.azarcafetero.auth_service.infrastructure.adapter;

import com.aguardiantes.azarcafetero.auth_service.domain.model.User;
import com.aguardiantes.azarcafetero.auth_service.domain.model.value.GoogleId;
import com.aguardiantes.azarcafetero.auth_service.domain.port.out.UserRepositoryPort;
import com.aguardiantes.azarcafetero.auth_service.infrastructure.persistence.JpaUserRepository;
import com.aguardiantes.azarcafetero.auth_service.infrastructure.persistence.UserJpaEntity;
import com.aguardiantes.azarcafetero.auth_service.infrastructure.persistence.UserMapper;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserRepositoryAdapter implements UserRepositoryPort {

    private final JpaUserRepository repository;

    public UserRepositoryAdapter(JpaUserRepository repository) {
        this.repository = repository;
    }

    @Override
    // ✅ Parámetro es GoogleId (Value Object), no String — coincide con el puerto
    public Optional<User> findByGoogleId(GoogleId googleId) {
        return repository.findByGoogleId(googleId.value()) // .value() extrae el String para JPA
                .map(UserMapper::toDomain);
    }

    @Override
    public User save(User user) {
        UserJpaEntity entity = UserMapper.toEntity(user);
        repository.save(entity);
        return user;
    }
}