package com.swiftly.persistence.user;

import com.swiftly.application.user.port.outbound.UserRepository;
import com.swiftly.domain.User;
import com.swiftly.persistence.entities.UserEntity;
import lombok.RequiredArgsConstructor;
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
        Optional<UserEntity> userEntityOptional = repository.findByEmail(email);

        if (userEntityOptional.isEmpty()) {
            return Optional.empty();
        }

        UserEntity userEntity = userEntityOptional.get();
        return Optional.of(new User(userEntity.getId(), userEntity.getEmail(), userEntity.getPasswordHash(), userEntity.getRoles()));
    }

    public User save(User user)
    {
        UserEntity userEntity = new UserEntity(user.getEmail(), user.getPasswordHash(), user.getRoles());
        if (user.getId() != null) {
            userEntity.setId(user.getId());
        }

        UserEntity savedEntity = repository.save(userEntity);
        return new User(savedEntity.getId(), savedEntity.getEmail(), savedEntity.getPasswordHash(), savedEntity.getRoles());
    }

    public User findById(Integer id)
    {
        Optional<UserEntity> userEntityOptional = repository.findById(id);

        if (userEntityOptional.isEmpty()) {
            return null;
        }
        UserEntity userEntity = userEntityOptional.get();
        return new User(userEntity.getId(), userEntity.getEmail(), userEntity.getPasswordHash(), userEntity.getRoles());
    }
}