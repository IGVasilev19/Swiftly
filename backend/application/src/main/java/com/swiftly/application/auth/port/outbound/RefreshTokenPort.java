package com.swiftly.application.auth.port.outbound;

import com.swiftly.domain.RefreshToken;

import java.util.Optional;

public interface RefreshTokenPort {
    Optional<RefreshToken> findByToken(String token);
    void deleteByEmail(String email);
    RefreshToken save(RefreshToken refreshToken);
    void delete(RefreshToken refreshToken);
}
