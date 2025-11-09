package com.swiftly.application.user.port.inbound;

import com.swiftly.domain.User;


public interface UserUseCase {
    User getByEmail(String email);
    Boolean existsByEmail(String email);
}
