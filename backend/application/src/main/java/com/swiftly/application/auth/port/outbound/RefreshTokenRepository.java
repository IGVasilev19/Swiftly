package com.swiftly.application.auth.port.outbound;

import com.swiftly.domain.RefreshToken;

public interface RefreshTokenRepository {
    RefreshToken findByToken(String token);
    void deleteById(Integer userId);
    RefreshToken save(RefreshToken refreshToken);
    void delete(RefreshToken refreshToken);
    RefreshToken findByUserId(Integer userId);
}
