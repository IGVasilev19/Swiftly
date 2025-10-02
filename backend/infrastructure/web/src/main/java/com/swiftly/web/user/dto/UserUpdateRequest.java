package com.swiftly.web.user.dto;

import com.swiftly.domain.enums.user.Role;

public record UserUpdateRequest(String fullName, String email, String password, Role role, Boolean status) {

}