package com.swiftly.application.auth.port.outbound;

import com.swiftly.domain.Profile;
import com.swiftly.domain.User;

public interface RegisterRepository {
    User saveNewUserAndProfile(User user, Profile profile);
}
