package com.swiftly.web.user.dto;

import com.swiftly.domain.enums.user.Role;

public record UserCreateRequest(String email, String password, Role role, Boolean status) {

}