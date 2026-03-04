package com.aguardiantes.azarcafetero.auth_service.domain.port.out;

import com.aguardiantes.azarcafetero.auth_service.domain.model.User;
import com.aguardiantes.azarcafetero.auth_service.domain.model.value.GoogleId;

import java.util.Optional;

public interface UserRepository {

    Optional<User> findByGoogleId(GoogleId googleId);

    User save(User user);
}