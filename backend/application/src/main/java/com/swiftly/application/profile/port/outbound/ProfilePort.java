package com.swiftly.application.profile.port.outbound;

import com.swiftly.domain.Profile;

import java.util.Optional;

public interface ProfilePort {
    Optional<Profile> findById(Integer id);
    Profile save(Profile profile);
}
