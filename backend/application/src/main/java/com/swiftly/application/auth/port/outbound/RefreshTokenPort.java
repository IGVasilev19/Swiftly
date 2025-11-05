package com.swiftly.application.auth.port.outbound;

import com.swiftly.domain.RefreshToken;

import java.util.Optional;

public interface RefreshTokenPort {
    RefreshToken findByToken(String token);
    void deleteByEmail(String email);
    RefreshToken save(RefreshToken refreshToken);
    void delete(RefreshToken refreshToken);
    RefreshToken findByUserId(Integer userId);
}
