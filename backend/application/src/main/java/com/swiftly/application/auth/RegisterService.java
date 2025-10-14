package com.swiftly.application.auth;

import com.swiftly.application.auth.dto.RegisterCommand;
import com.swiftly.application.auth.port.inbound.RegisterUseCase;
import com.swiftly.application.auth.port.outbound.RegisterPort;
import com.swiftly.application.helpers.PasswordHasher;
import com.swiftly.application.user.port.inbound.UserUseCase;
import com.swiftly.domain.Profile;
import com.swiftly.domain.User;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class RegisterService implements RegisterUseCase {
    private final RegisterPort registerPort;
    private final UserUseCase userUseCase;


    public RegisterService(RegisterPort registerPort,  UserUseCase userUseCase)
    {
        this.registerPort = registerPort;
        this.userUseCase = userUseCase;
    }

    @Transactional
    public User register(RegisterCommand registerUser)
    {
        if(userUseCase.existsByEmail(registerUser.email()))
        {
            throw new IllegalArgumentException("User already exists");
        }

        User newUser = new User(registerUser.email(), PasswordHasher.hashPassword(registerUser.password()), registerUser.role(), false);

        Profile newProfile = new Profile(newUser, registerUser.fullName(), registerUser.phoneNumber(), registerUser.address(), registerUser.city(), registerUser.country(), registerUser.postalCode());

        return registerPort.saveNewUserAndProfile(newUser, newProfile);
    }
}
