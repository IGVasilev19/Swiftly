package com.swiftly.application.auth.port.inbound;

import com.swiftly.domain.RefreshToken;


public interface RefreshTokenUseCase {
    RefreshToken getByToken(String token);
    void deleteTokenByEmail(String email);
    RefreshToken getByUserId(Integer userId);
}
