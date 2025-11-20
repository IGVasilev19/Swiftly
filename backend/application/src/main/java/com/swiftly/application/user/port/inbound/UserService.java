package com.swiftly.application.user.port.inbound;

import com.swiftly.domain.User;


public interface UserService {
    User getByEmail(String email);
    Boolean existsByEmail(String email);
}
