package com.swiftly.application.profile.port.inbound;

import com.swiftly.domain.Profile;

import java.util.Optional;


public interface ProfileService {
    Profile getById(Integer id);
}
