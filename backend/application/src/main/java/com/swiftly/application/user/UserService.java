package com.swiftly.application.user;

import com.swiftly.application.user.port.inbound.UserUseCase;
import com.swiftly.application.user.port.outbound.UserPort;
import com.swiftly.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;



@Service
@RequiredArgsConstructor
public class UserService implements UserUseCase {
    private final UserPort userPort;


    public User getByEmail(String email)
    {
        User user = userPort.findByEmail(email);
        if (user == null) {
            throw new IllegalArgumentException("User not found");
        }
        return user;
    }

    public Boolean existsByEmail(String email)
    {
        return userPort.existsByEmail(email);
    }

}