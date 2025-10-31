package com.swiftly.persistence.user;

import com.swiftly.application.user.port.outbound.UserPort;
import com.swiftly.domain.User;
import com.swiftly.persistence.entities.UserEntity;
import org.springframework.stereotype.Component;


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

    public User findByEmail(String email)
    {
        UserEntity userEntity = repository.findByEmail(email);
        return userEntity; // preserve id and fields; UserEntity extends domain User
    }

    public User save(User user)
    {
        UserEntity userEntity = new UserEntity(user.getEmail(), user.getPasswordHash(), user.getRole());

        return repository.save(userEntity);
    }

    @Override
    public User findById(Integer id) {
        return repository.findById(id).orElse(null);
    }
}