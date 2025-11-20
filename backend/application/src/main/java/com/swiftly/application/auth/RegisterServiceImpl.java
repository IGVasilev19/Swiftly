package com.swiftly.application.auth;

import com.swiftly.application.auth.dto.RegisterCommand;
import com.swiftly.application.auth.port.inbound.RegisterService;
import com.swiftly.application.auth.port.outbound.RegisterRepository;
import com.swiftly.application.helpers.PasswordHasher;
import com.swiftly.application.user.port.inbound.UserService;
import com.swiftly.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RegisterServiceImpl implements RegisterService {
    private final RegisterRepository registerRepository;
    private final UserService userService;


    public User register(RegisterCommand  registerAccount)
    {
        if(userService.existsByEmail(registerAccount.user().getEmail()))
        {
            throw new IllegalArgumentException("User already exists");
        }

        registerAccount.user().setPasswordHash(PasswordHasher.hashPassword(registerAccount.user().getPasswordHash()));

        return registerRepository.saveNewUserAndProfile(registerAccount.user(), registerAccount.profile());
    }
}
