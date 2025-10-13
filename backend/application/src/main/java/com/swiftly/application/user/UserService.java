package com.swiftly.application.user;

import com.swiftly.application.user.port.inbound.UserUseCase;
import com.swiftly.application.user.port.outbound.UserPort;
import com.swiftly.domain.User;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserService implements UserUseCase {
    private final UserPort userPort;

    public UserService(UserPort userPort)
    {
        this.userPort = userPort;
    }

    public User getById(Integer id)
    {
        return userPort.findById(id).orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

    public User create(User newUser)
    {
        if (userPort.existsByEmail(newUser.getEmail()))
        {
            throw new DuplicateKeyException("Email already in use");
        }

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        newUser.setPasswordHash(passwordEncoder.encode(newUser.getPasswordHash()));
        return userPort.save(newUser);
    }

    public User getByEmail(String email)
    {
        return userPort.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

}