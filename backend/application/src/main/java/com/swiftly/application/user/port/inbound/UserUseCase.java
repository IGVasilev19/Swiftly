package com.swiftly.application.user.port.inbound;

import com.swiftly.domain.User;

public interface UserUseCase {
    User getById(Integer id);
    User getByEmail(String email);
    User create(User user);
}
