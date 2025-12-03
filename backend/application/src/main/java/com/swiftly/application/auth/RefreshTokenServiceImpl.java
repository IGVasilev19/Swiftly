package com.swiftly.application.auth;

import com.swiftly.application.auth.port.inbound.RefreshTokenService;
import com.swiftly.application.auth.port.outbound.RefreshTokenRepository;
import com.swiftly.application.user.port.inbound.UserService;
import com.swiftly.domain.RefreshToken;
import com.swiftly.domain.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final UserService userService;

    @Value("${jwt.refresh.expiration}")
    private long refreshExpiration;

    public RefreshToken createRefreshToken(String email) {
        User user = userService.getByEmail(email);

        Optional<RefreshToken> existing = getByUserId(user.getId());

        if (existing.isPresent()) {
            RefreshToken valid = verifyExpiration(existing.get());
            if (valid != null) {
                return valid;
            }
        }

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUser(user);
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setExpiryDate(Instant.now().plusMillis(refreshExpiration));
        refreshToken.setRevoked(false);

        return refreshTokenRepository.save(refreshToken);
    }

    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().isBefore(Instant.now()) || token.isRevoked()) {

            refreshTokenRepository.delete(token);

            return null;
        }
        return token;
    }

    public void deleteTokenById(Integer userId) {
        refreshTokenRepository.deleteById(userId);
    }

    @Transactional
    public RefreshToken getByToken(String token)
    {
        return refreshTokenRepository.findByToken(token);
    }

    public Optional<RefreshToken> getByUserId(Integer userId)
    {
        return refreshTokenRepository.findByUserId(userId);
    }
}
