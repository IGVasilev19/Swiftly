package com.swiftly.web.auth.mapper;

import com.swiftly.application.auth.dto.RegisterCommand;
import com.swiftly.domain.Profile;
import com.swiftly.domain.User;
import com.swiftly.web.auth.dto.RegisterRequest;

public class RegisterMapper {
    public static RegisterCommand toCommand(RegisterRequest registerRequest)
    {
        User user = new User(registerRequest.email(), registerRequest.password(), registerRequest.role());
        Profile profile = new Profile(registerRequest.name(), registerRequest.phoneNumber(), registerRequest.address(), registerRequest.city(), registerRequest.country(), registerRequest.postalCode());
        user.attachProfile(profile);
        return new RegisterCommand(user, profile);
    }
}
