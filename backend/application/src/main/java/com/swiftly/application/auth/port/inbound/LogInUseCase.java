package com.swiftly.application.auth.port.inbound;

import com.swiftly.domain.User;

import java.util.Optional;

public interface LogInUseCase {
    User login(User user);
}
