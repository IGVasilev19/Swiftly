package com.swiftly.application.auth;

import com.swiftly.application.auth.port.inbound.RefreshTokenUseCase;
import com.swiftly.application.auth.port.outbound.RefreshTokenPort;
import com.swiftly.application.user.port.outbound.UserPort;
import com.swiftly.domain.RefreshToken;
import com.swiftly.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenService implements RefreshTokenUseCase {

    private final RefreshTokenPort refreshTokenPort;
    private final UserPort userPort;

    @Value("${jwt.refresh.expiration}")
    private long refreshExpiration;

    public RefreshToken createRefreshToken(String email) {

        User user = userPort.findByEmail(email);

        if(user==null)
        {
            throw(new RuntimeException("User not found"));
        }

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

    public void deleteTokenByEmail(String email) {
        refreshTokenPort.deleteByEmail(email);
    }

    public Optional<RefreshToken> getByToken(String token)
    {
        return refreshTokenPort.findByToken(token);
    }

    public RefreshToken getByUserId(Integer userId)
    {
        return refreshTokenPort.findByUserId(userId);
    }
}
