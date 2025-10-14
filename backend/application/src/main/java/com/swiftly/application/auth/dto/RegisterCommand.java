package com.swiftly.application.auth.dto;

import com.swiftly.domain.enums.user.Role;

public record RegisterCommand (String email, String password, String fullName, String phoneNumber, Role role, String address, String city, String country, String postalCode) { }
