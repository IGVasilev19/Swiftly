package com.swiftly.application.profile;

import com.swiftly.application.profile.port.inbound.ProfileUseCase;
import com.swiftly.application.profile.port.outbound.ProfilePort;
import com.swiftly.domain.Profile;
import org.springframework.stereotype.Service;

@Service
public class ProfileService implements ProfileUseCase {
    private final ProfilePort profilePort;

    public ProfileService(ProfilePort profilePort)
    {
        this.profilePort = profilePort;
    }

    public Profile getById(Integer id)
    {
        return profilePort.findById(id).orElseThrow(() -> new IllegalArgumentException("Profile not found"));
    }
}
