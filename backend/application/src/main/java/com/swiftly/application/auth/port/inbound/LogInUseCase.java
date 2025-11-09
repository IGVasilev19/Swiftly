package com.swiftly.application.auth.port.inbound;

import com.swiftly.domain.User;


public interface LogInUseCase {
    User login(User user);
    User refreshToken(String token);
    void logout(Integer userId);
}
