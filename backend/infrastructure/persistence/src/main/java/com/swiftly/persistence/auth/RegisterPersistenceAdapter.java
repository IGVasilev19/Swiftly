package com.swiftly.persistence.auth;

import com.swiftly.application.auth.port.outbound.RegisterPort;
import com.swiftly.application.profile.port.outbound.ProfilePort;
import com.swiftly.application.user.port.outbound.UserPort;
import com.swiftly.domain.Profile;
import com.swiftly.domain.User;
import com.swiftly.persistence.entities.ProfileEntity;
import com.swiftly.persistence.entities.UserEntity;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Component;

@Component
public class RegisterPersistenceAdapter implements RegisterPort {
    private final UserPort userPort;
    private final ProfilePort profilePort;

    public RegisterPersistenceAdapter(UserPort userPort, ProfilePort profilePort)
    {
        this.userPort = userPort;
        this.profilePort = profilePort;
    }

    @Transactional
    public User saveNewUserAndProfile(User user, Profile profile)
    {

        UserEntity userEntity = new UserEntity(user.getEmail(), user.getPasswordHash(), user.getRole(), false);

        ProfileEntity profileEntity = new ProfileEntity(userEntity, profile.getFullName(), profile.getPhone(), profile.getAddress(), profile.getCity(), profile.getCountry(), profile.getPostalCode());

        profilePort.save(profile);
        return  userPort.save(user);
    }
}
