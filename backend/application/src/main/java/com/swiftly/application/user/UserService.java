package com.swiftly.application.user;

import com.swiftly.application.user.port.inbound.UserUseCase;
import com.swiftly.application.user.port.outbound.UserPort;
import com.swiftly.domain.User;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class UserService implements UserUseCase {
    private final UserPort userPort;

    public UserService(UserPort userPort)
    {
        this.userPort = userPort;
    }

    public Optional<User> getByEmail(String email)
    {
        return Optional.ofNullable(userPort.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("User not found")));
    }

    public Boolean existsByEmail(String email)
    {
        return userPort.existsByEmail(email);
    }

}