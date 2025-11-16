package com.swiftly.persistence.auth;

import com.swiftly.application.auth.port.outbound.RefreshTokenPort;
import com.swiftly.domain.RefreshToken;
import com.swiftly.persistence.entities.RefreshTokenEntity;
import com.swiftly.persistence.entities.UserEntity;
import com.swiftly.persistence.user.JpaUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class RefreshTokenPersistenceAdapter implements RefreshTokenPort {
    private final JpaRefreshTokenRepository jpaRefreshTokenRepository;
    private final JpaUserRepository  userRepository;

    public RefreshToken findByToken(String refreshToken)
    {
        Optional<RefreshTokenEntity> tokenEntity = jpaRefreshTokenRepository.findByToken(refreshToken);

        return new RefreshToken(tokenEntity.get().getId(), tokenEntity.get().getToken(), tokenEntity.get().getExpiryDate(), tokenEntity.get().getUser(), tokenEntity.get().isRevoked());
    }

    public void deleteById(Integer userId)
    {
        jpaRefreshTokenRepository.deleteByUserId(userId);
    }

    public RefreshToken save(RefreshToken refreshToken)
    {

        UserEntity userEntity =  userRepository.findById(refreshToken.getUser().getId()).orElse(null);

        RefreshTokenEntity refreshTokenEntity = new RefreshTokenEntity(refreshToken.getToken(), refreshToken.getExpiryDate(), userEntity, refreshToken.isRevoked());

        return jpaRefreshTokenRepository.save(refreshTokenEntity);
    }

    public void delete(RefreshToken refreshToken)
    {
        UserEntity userEntity = new UserEntity(refreshToken.getUser().getEmail(), refreshToken.getUser().getPasswordHash(), refreshToken.getUser().getRole());

        RefreshTokenEntity refreshTokenEntity = new RefreshTokenEntity(refreshToken.getId(), refreshToken.getToken(), refreshToken.getExpiryDate(), userEntity, refreshToken.isRevoked());

        jpaRefreshTokenRepository.delete(refreshTokenEntity);
    }

    public RefreshToken findByUserId(Integer userId)
    {
        RefreshTokenEntity refreshTokenEntity = jpaRefreshTokenRepository.findByUserId(userId);

        return new RefreshToken(refreshTokenEntity.getId(), refreshTokenEntity.getToken(), refreshTokenEntity.getExpiryDate(), refreshTokenEntity.getUser(), refreshTokenEntity.isRevoked());
    }
}
