package com.swiftly.application.auth;

import com.swiftly.application.auth.port.inbound.LogInUseCase;
import com.swiftly.application.helpers.PasswordHasher;
import com.swiftly.application.user.port.outbound.UserPort;
import com.swiftly.domain.User;

import java.util.Optional;

public class LogInService implements LogInUseCase {
    private final UserPort userPort;

    public LogInService(UserPort userPort)
    {
        this.userPort = userPort;
    }

    public Optional<User> logIn(String email, String password)
    {
        Optional<User> userToLogIn = userPort.findByEmail(email);

        if(userToLogIn.isPresent())
        {
            if(PasswordHasher.checkPassword(password, userToLogIn.get().getPasswordHash()))
            {
                return  userToLogIn;
            }
            else {
                throw new IllegalArgumentException("Wrong password");
            }
        }
        else {
            throw new IllegalArgumentException("Account doesn't exist");
        }
    }
}
