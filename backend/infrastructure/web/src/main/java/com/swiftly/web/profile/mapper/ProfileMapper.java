package com.swiftly.web.profile.mapper;

import com.swiftly.domain.Profile;
import com.swiftly.web.profile.dto.ProfileResponse;

public class ProfileMapper {
    public static ProfileResponse toResponse(Profile profile)
    {
        return new ProfileResponse(profile.getId(), profile.getFullName(), profile.getPhone(), profile.getAvatarUrl());
    }
}
