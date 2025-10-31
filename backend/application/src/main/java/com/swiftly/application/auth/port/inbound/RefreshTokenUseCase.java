package com.swiftly.application.auth.port.inbound;

import com.swiftly.domain.RefreshToken;

import java.util.Optional;

public interface RefreshTokenUseCase {
    Optional<RefreshToken> getByToken(String token);
    void deleteTokenByEmail(String email);
}
