package com.swiftly.application.auth.port.inbound;

import com.swiftly.domain.RefreshToken;

import java.util.Optional;


public interface RefreshTokenService {
    RefreshToken getByToken(String token);
    void deleteTokenById(Integer userId);
    Optional<RefreshToken> getByUserId(Integer userId);
    RefreshToken createRefreshToken(String email);
    RefreshToken verifyExpiration(RefreshToken token);
}
