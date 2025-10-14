package com.swiftly.application.profile.port.inbound;

import com.swiftly.domain.Profile;


public interface ProfileUseCase {
    Profile getById(Integer id);
}
