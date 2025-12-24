package com.swiftly.persistence.profile;

import com.swiftly.application.profile.port.outbound.ProfileRepository;
import com.swiftly.domain.Profile;
import com.swiftly.persistence.entities.ProfileEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class ProfilePersistenceImpl implements ProfileRepository {
    private final JpaProfileRepository repository;
    private final com.swiftly.persistence.user.JpaUserRepository userRepository;


    public Profile findById(Integer id)
    {
        Optional<ProfileEntity> profileEntityOptional = repository.findById(id);

        if (profileEntityOptional.isEmpty()) return null;

        ProfileEntity entity = profileEntityOptional.get();

        return new Profile(entity.getId(), entity.getFullName(), entity.getPhone(), entity.getAvatarUrl());
    }

    public Profile save(Profile profile)
    {
        if (profile.getUser() == null || profile.getUser().getId() == null) {
             throw new IllegalArgumentException("Profile must have a user with ID");
        }

        com.swiftly.persistence.entities.UserEntity userEntity = userRepository.findById(profile.getUser().getId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        
        ProfileEntity profileEntity = new ProfileEntity(userEntity, profile.getFullName(), profile.getPhone(), profile.getAvatarUrl());
        
        if (profile.getId() != null) {
            profileEntity.setId(profile.getId());
        }

        ProfileEntity savedEntity = repository.save(profileEntity);
        return new Profile(savedEntity.getId(), savedEntity.getFullName(), savedEntity.getPhone(), savedEntity.getAvatarUrl());
    }
}
