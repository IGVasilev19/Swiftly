package com.swiftly.application.user.port.outbound;
import com.swiftly.domain.User;


public interface UserPort {
    User save(User user);
    Boolean existsByEmail(String email);
    User findByEmail(String email);
    User findById(Integer id);
}