package com.swiftly.persistence.user;

import com.swiftly.application.user.port.outbound.UserPort;
import com.swiftly.domain.User;
import com.swiftly.persistence.entities.UserEntity;
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
        Optional<UserEntity> userEntity = repository.findByEmail(email);

        return Optional.of(userEntity.get());
    }

    public User save(User user)
    {
        UserEntity userEntity = new UserEntity(user.getEmail(), user.getPasswordHash(), user.getRole(), user.getStatus());

        return repository.save(userEntity);
    }
}