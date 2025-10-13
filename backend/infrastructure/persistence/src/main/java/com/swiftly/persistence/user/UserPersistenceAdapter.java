package com.swiftly.persistence.user;

import com.swiftly.application.user.port.outbound.UserPort;
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

    @Override
    public Optional<User> findById(Integer id)
    {
        return repository.findById(id);
    }

    @Override
    public boolean existsByEmail(String email)
    {
        return repository.existsByEmail(email);
    }

    @Override
    public Optional<User> findByEmail(String email)
    {
        return repository.findByEmail(email);
    }

    @Override
    public User save(User user)
    {
        return repository.save(user);
    }
}