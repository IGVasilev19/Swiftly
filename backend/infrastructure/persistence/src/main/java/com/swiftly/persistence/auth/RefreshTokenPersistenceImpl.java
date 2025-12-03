package com.swiftly.persistence.auth;

import com.swiftly.application.auth.port.outbound.RefreshTokenRepository;
import com.swiftly.domain.RefreshToken;
import com.swiftly.domain.User;
import com.swiftly.persistence.entities.RefreshTokenEntity;
import com.swiftly.persistence.entities.UserEntity;
import com.swiftly.persistence.user.JpaUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class RefreshTokenPersistenceImpl implements RefreshTokenRepository {
    private final JpaRefreshTokenRepository jpaRefreshTokenRepository;
    private final JpaUserRepository  userRepository;

    public RefreshToken findByToken(String refreshToken)
    {
        Optional<RefreshTokenEntity> tokenEntity = jpaRefreshTokenRepository.findByToken(refreshToken);

        if (tokenEntity.isEmpty()) {
            return null;
        }

        RefreshTokenEntity entity = tokenEntity.get();
        UserEntity userEntity = entity.getUser();
        
        return new RefreshToken(entity.getId(), entity.getToken(), entity.getExpiryDate(), 
                new User(userEntity.getId(), userEntity.getEmail(), userEntity.getPasswordHash(), userEntity.getRoles()), 
                entity.isRevoked());
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
        UserEntity userEntity = new UserEntity(refreshToken.getUser().getEmail(), refreshToken.getUser().getPasswordHash(), refreshToken.getUser().getRoles());

        RefreshTokenEntity refreshTokenEntity = new RefreshTokenEntity(refreshToken.getId(), refreshToken.getToken(), refreshToken.getExpiryDate(), userEntity, refreshToken.isRevoked());

        jpaRefreshTokenRepository.delete(refreshTokenEntity);
    }

    public Optional<RefreshToken> findByUserId(Integer userId)
    {
        Optional<RefreshTokenEntity> refreshTokenEntity = jpaRefreshTokenRepository.findByUserId(userId);

        return refreshTokenEntity.map(entity -> new RefreshToken(entity.getId(), entity.getToken(), entity.getExpiryDate(), entity.getUser(), entity.isRevoked()));
    }
}
