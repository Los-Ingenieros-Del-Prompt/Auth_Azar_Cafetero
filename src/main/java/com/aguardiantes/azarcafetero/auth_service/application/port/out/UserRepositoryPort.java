package com.aguardiantes.azarcafetero.auth_service.application.port.out;

import com.aguardiantes.azarcafetero.auth_service.domain.model.User;
import com.aguardiantes.azarcafetero.auth_service.domain.model.value.GoogleId;

import java.util.Optional;

public interface UserRepositoryPort {

    Optional<User> findByGoogleId(GoogleId googleId);

    User save(User user);
}