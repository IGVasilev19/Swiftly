package com.swiftly.application.user;

import com.swiftly.application.user.port.inbound.UserService;
import com.swiftly.application.user.port.outbound.UserRepository;
import com.swiftly.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;


    public User getByEmail(String email)
    {
        Optional<User> user = userRepository.findByEmail(email);

        User existingUser = user.orElseThrow(() -> new IllegalArgumentException("User not found"));
        
        return existingUser;
    }

    public Boolean existsByEmail(String email)
    {
        return userRepository.existsByEmail(email);
    }
}