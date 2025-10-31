package com.swiftly.application.auth;

import com.swiftly.application.auth.dto.RegisterCommand;
import com.swiftly.application.auth.port.inbound.RegisterUseCase;
import com.swiftly.application.auth.port.outbound.RegisterPort;
import com.swiftly.application.helpers.PasswordHasher;
import com.swiftly.application.user.port.inbound.UserUseCase;
import com.swiftly.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RegisterService implements RegisterUseCase {
    private final RegisterPort registerPort;
    private final UserUseCase userUseCase;


    public User register(RegisterCommand  registerAccount)
    {
        if(userUseCase.existsByEmail(registerAccount.user().getEmail()))
        {
            throw new IllegalArgumentException("User already exists");
        }

        registerAccount.user().setPasswordHash(PasswordHasher.hashPassword(registerAccount.user().getPasswordHash()));

        return registerPort.saveNewUserAndProfile(registerAccount.user(), registerAccount.profile());
    }
}
