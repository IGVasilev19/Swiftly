package com.swiftly.application.user.port.outbound;
import com.swiftly.domain.User;

import java.util.Optional;


public interface UserPort {
    User save(User user);
    Boolean existsByEmail(String email);
    Optional<User> findByEmail(String email);
    User findById(Integer userId);
}