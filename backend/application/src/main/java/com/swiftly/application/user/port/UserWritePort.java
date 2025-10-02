package com.swiftly.application.user.port;
import com.swiftly.domain.User;

public interface UserWritePort {
    User save(User user);
    void deleteById(Integer id);
}