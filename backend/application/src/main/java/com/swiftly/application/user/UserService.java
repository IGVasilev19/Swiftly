package com.swiftly.application.user;

import com.swiftly.application.user.port.inbound.UserUseCase;
import com.swiftly.application.user.port.outbound.UserPort;
import com.swiftly.domain.User;
import org.springframework.stereotype.Service;


@Service
public class UserService implements UserUseCase {
    private final UserPort userPort;

    public UserService(UserPort userPort)
    {
        this.userPort = userPort;
    }

    public User getByEmail(String email)
    {
        return userPort.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

    public Boolean existsByEmail(String email)
    {
        return userPort.existsByEmail(email);
    }

}