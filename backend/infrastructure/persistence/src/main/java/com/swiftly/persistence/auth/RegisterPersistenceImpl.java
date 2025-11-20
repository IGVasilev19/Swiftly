package com.swiftly.persistence.auth;

import com.swiftly.application.auth.port.outbound.RegisterRepository;
import com.swiftly.application.profile.port.outbound.ProfileRepository;
import com.swiftly.application.user.port.outbound.UserRepository;
import com.swiftly.domain.Profile;
import com.swiftly.domain.User;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Component;

@Component
public class RegisterPersistenceImpl implements RegisterRepository {
    private final UserRepository userRepository;
    private final ProfileRepository profileRepository;

    public RegisterPersistenceImpl(UserRepository userRepository, ProfileRepository profileRepository)
    {
        this.userRepository = userRepository;
        this.profileRepository = profileRepository;
    }

    @Transactional
    public User saveNewUserAndProfile(User user, Profile profile)
    {
        User savedUser = userRepository.save(user);

        profile.setUser(savedUser);

        profileRepository.save(profile);
        
        return savedUser;
    }
}
