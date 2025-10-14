package com.swiftly.persistence.user;

import com.swiftly.application.user.port.outbound.UserPort;
import com.swiftly.domain.Booking;
import com.swiftly.domain.User;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserPersistenceAdapter implements UserPort {

    private final JpaUserRepository repository;

    public UserPersistenceAdapter(JpaUserRepository repository)
    {
        this.repository = repository;
    }

    public Boolean existsByEmail(String email)
    {
        return repository.existsByEmail(email);
    }

    public Optional<User> findByEmail(String email)
    {
        return repository.findByEmail(email);
    }

    public User save(User user)
    {
        return repository.save(user);
    }
}