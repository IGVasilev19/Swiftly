package com.swiftly.web.auth.dto;

import com.swiftly.domain.enums.user.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record RegisterRequest(@Schema(description = "User email address", example = "user@example.com", format = "email") @Email @NotBlank String email, @NotBlank String password, @NotBlank String name, @NotBlank @Pattern(regexp = "^\\+?[1-9]\\d{6,14}$") String phoneNumber, @NotNull(message = "Role is required") Role role, @NotBlank String address, @NotBlank String city, @NotBlank String country, @NotBlank @Pattern(regexp = "^[A-Za-z0-9\\- ]{3,10}$") String postalCode) { }
