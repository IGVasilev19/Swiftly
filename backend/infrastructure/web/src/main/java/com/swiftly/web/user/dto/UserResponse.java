package com.swiftly.web.user.dto;

import com.swiftly.domain.enums.user.Role;

public record UserResponse(Integer id, String email, Role role, Boolean status) {

}