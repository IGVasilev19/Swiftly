package com.swiftly.application.auth.port.inbound;

import com.swiftly.domain.RefreshToken;


public interface RefreshTokenUseCase {
    RefreshToken getByToken(String token);
    void deleteTokenById(Integer userId);
    RefreshToken getByUserId(Integer userId);
    RefreshToken createRefreshToken(String email);
    RefreshToken verifyExpiration(RefreshToken token);
}
