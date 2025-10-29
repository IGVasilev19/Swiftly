package com.swiftly.application.auth.port.inbound;

import com.swiftly.domain.User;

import java.util.Optional;

public interface LogInUseCase {
    Optional<User> logIn(String username, String password);
}
