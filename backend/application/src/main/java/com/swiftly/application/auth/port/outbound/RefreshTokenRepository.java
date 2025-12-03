package com.swiftly.application.auth.port.outbound;

import com.swiftly.domain.RefreshToken;

import java.util.Optional;

public interface RefreshTokenRepository {
    RefreshToken findByToken(String token);
    void deleteById(Integer userId);
    RefreshToken save(RefreshToken refreshToken);
    void delete(RefreshToken refreshToken);
    Optional<RefreshToken> findByUserId(Integer userId);
}
