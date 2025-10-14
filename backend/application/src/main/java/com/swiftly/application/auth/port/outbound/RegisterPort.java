package com.swiftly.application.auth.port.outbound;

import com.swiftly.domain.Profile;
import com.swiftly.domain.User;

public interface RegisterPort {
    User saveNewUserAndProfile(User user, Profile profile);
}
