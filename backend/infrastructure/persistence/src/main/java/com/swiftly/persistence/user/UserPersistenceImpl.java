package com.swiftly.persistence.user;

import com.swiftly.application.user.port.outbound.UserRepository;
import com.swiftly.domain.User;
import com.swiftly.persistence.entities.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@RequiredArgsConstructor
@Repository
public class UserPersistenceImpl implements UserRepository {

    private final JpaUserRepository repository;

    public Boolean existsByEmail(String email)
    {
        return repository.existsByEmail(email);
    }

    public Optional<User> findByEmail(String email)
    {
        Optional<UserEntity> userEntity = repository.findByEmail(email);

        return Optional.of(new User(userEntity.get().getId(), userEntity.get().getEmail(), userEntity.get().getPasswordHash(), userEntity.get().getRoles()));
    }

    public UserEntity save(User user)
    {
        UserEntity userEntity = new UserEntity(user.getEmail(), user.getPasswordHash(), user.getRoles());

        return repository.save(userEntity);
    }

    public User findById(Integer id)
    {
        Optional<UserEntity> userEntity = repository.findById(id);

        return new User(userEntity.get().getId(), userEntity.get().getEmail(), userEntity.get().getPasswordHash(), userEntity.get().getRoles());
    }
}