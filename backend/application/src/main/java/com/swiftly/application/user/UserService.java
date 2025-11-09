package com.swiftly.application.user;

import com.swiftly.application.user.port.inbound.UserUseCase;
import com.swiftly.application.user.port.outbound.UserPort;
import com.swiftly.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
@RequiredArgsConstructor
public class UserService implements UserUseCase {
    private final UserPort userPort;


    public User getByEmail(String email)
    {
        Optional<User> user = userPort.findByEmail(email);

        User existingUser = user.orElseThrow(() -> new IllegalArgumentException("User not found"));
        
        return existingUser;
    }

    public Boolean existsByEmail(String email)
    {
        return userPort.existsByEmail(email);
    }
}