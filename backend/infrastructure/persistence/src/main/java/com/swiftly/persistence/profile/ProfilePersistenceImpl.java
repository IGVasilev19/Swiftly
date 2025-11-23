package com.swiftly.persistence.profile;

import com.swiftly.application.profile.port.outbound.ProfileRepository;
import com.swiftly.domain.Profile;
import com.swiftly.persistence.entities.ProfileEntity;
import com.swiftly.persistence.entities.UserEntity;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ProfilePersistenceImpl implements ProfileRepository {
    private final JpaProfileRepository repository;

    public ProfilePersistenceImpl(JpaProfileRepository repository)
    {
        this.repository = repository;
    }

    public Optional<Profile> findById(Integer id)
    {
        Optional<ProfileEntity> profileEntity = repository.findById(id);

        return Optional.of(profileEntity.get());
    }

    public Profile save(Profile profile)
    {
        UserEntity userEntity = (UserEntity) profile.getUser();
        
        ProfileEntity profileEntity = new ProfileEntity(userEntity, profile.getFullName(), profile.getPhone(), null);

        return repository.save(profileEntity);
    }
}
