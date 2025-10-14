package com.swiftly.persistence.profile;

import com.swiftly.application.profile.port.outbound.ProfilePort;
import com.swiftly.domain.Profile;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ProfilePersistenceAdapter implements ProfilePort {
    private final JpaProfileRepository repository;

    public ProfilePersistenceAdapter(JpaProfileRepository repository)
    {
        this.repository = repository;
    }

    public Optional<Profile> findById(Integer id)
    {
        return repository.findById(id);
    }

    public Profile save(Profile profile)
    {
        return repository.save(profile);
    }
}
