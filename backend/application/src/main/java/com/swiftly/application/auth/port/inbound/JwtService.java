package com.swiftly.application.auth.port.inbound;

import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public interface JwtService {
    String generateAccessToken(UserDetails userDetails);
    String extractUsername(String token);
    Integer extractUserId(String token);
    public List<String> extractRoles(String token);
    boolean isValid(String token, Integer userId);
}
