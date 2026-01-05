package com.swiftly.web.auth.dto;

import jakarta.validation.constraints.NotBlank;

public record LogInRequest (@NotBlank String email, @NotBlank String password) { }
