package com.swiftly.application.auth;

import com.swiftly.application.auth.port.inbound.RefreshTokenUseCase;
import com.swiftly.application.auth.port.outbound.RefreshTokenPort;
import com.swiftly.application.user.port.inbound.UserUseCase;
import com.swiftly.domain.RefreshToken;
import com.swiftly.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenService implements RefreshTokenUseCase {

    private final RefreshTokenPort refreshTokenPort;
    private final UserUseCase userService;

    @Value("${jwt.refresh.expiration}")
    private long refreshExpiration;

    public RefreshToken createRefreshToken(String email) {

        User user = userService.getByEmail(email);

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUser(user);
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setExpiryDate(Instant.now().plusMillis(refreshExpiration));
        refreshToken.setRevoked(false);
        return refreshTokenPort.save(refreshToken);
    }

    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().isBefore(Instant.now()) || token.isRevoked()) {

            refreshTokenPort.delete(token);

            return null;
        }
        return token;
    }

    public void deleteTokenById(Integer userId) {
        refreshTokenPort.deleteById(userId);
    }

    public RefreshToken getByToken(String token)
    {
        return refreshTokenPort.findByToken(token);
    }

    public RefreshToken getByUserId(Integer userId)
    {
        return refreshTokenPort.findByUserId(userId);
    }
}
