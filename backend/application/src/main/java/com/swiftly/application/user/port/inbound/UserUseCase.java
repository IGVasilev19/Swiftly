package com.swiftly.application.user.port.inbound;

import com.swiftly.domain.User;

import java.util.Optional;


public interface UserUseCase {
    Optional<User> getByEmail(String email);
    Boolean existsByEmail(String email);
}
