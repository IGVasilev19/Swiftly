package com.swiftly.web.user.dto;

public record UserResponse(Integer id, String fullName, String email, com.swiftly.domain.enums.user.Role role, Boolean status) {

}