package com.swiftly.application.auth.dto;

import com.swiftly.domain.Profile;
import com.swiftly.domain.User;

public record RegisterCommand (User user, Profile profile) { }
