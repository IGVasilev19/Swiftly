package com.swiftly.persistence.auth;

import com.swiftly.application.auth.port.outbound.RefreshTokenRepository;
import com.swiftly.domain.RefreshToken;
import com.swiftly.domain.User;
import com.swiftly.persistence.entities.RefreshTokenEntity;
import com.swiftly.persistence.entities.UserEntity;
import com.swiftly.persistence.user.JpaUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class RefreshTokenPersistenceImpl implements RefreshTokenRepository {
    private final JpaRefreshTokenRepository jpaRefreshTokenRepository;
    private final JpaUserRepository  userRepository;

    public RefreshToken findByToken(String refreshToken)
    {
        Optional<RefreshTokenEntity> tokenEntityOptional = jpaRefreshTokenRepository.findByToken(refreshToken);

        if (tokenEntityOptional.isEmpty()) {
            return null;
        }

        RefreshTokenEntity entity = tokenEntityOptional.get();
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
        if (userEntity == null) {
            throw new IllegalArgumentException("User not found for refresh token");
        }

        RefreshTokenEntity refreshTokenEntity = new RefreshTokenEntity(refreshToken.getToken(), refreshToken.getExpiryDate(), userEntity, refreshToken.isRevoked());
        if (refreshToken.getId() != null) {
            refreshTokenEntity.setId(refreshToken.getId());
        }

        RefreshTokenEntity savedEntity = jpaRefreshTokenRepository.save(refreshTokenEntity);
        return new RefreshToken(savedEntity.getId(), savedEntity.getToken(), savedEntity.getExpiryDate(), 
                new User(userEntity.getId(), userEntity.getEmail(), userEntity.getPasswordHash(), userEntity.getRoles()), 
                savedEntity.isRevoked());
    }

    public void delete(RefreshToken refreshToken)
    {
         if (refreshToken.getId() != null) {
             jpaRefreshTokenRepository.deleteById(refreshToken.getId());
         } else {
             // If no ID, we might need to look it up or constructing entity for delete might fail if detached
             // Fallback to token
             Optional<RefreshTokenEntity> existing = jpaRefreshTokenRepository.findByToken(refreshToken.getToken());
             existing.ifPresent(jpaRefreshTokenRepository::delete);
         }
    }

    public Optional<RefreshToken> findByUserId(Integer userId)
    {
        Optional<RefreshTokenEntity> refreshTokenEntityOptional = jpaRefreshTokenRepository.findByUserId(userId);

        if (refreshTokenEntityOptional.isEmpty()) return Optional.empty();
        
        RefreshTokenEntity entity = refreshTokenEntityOptional.get();
        UserEntity userEntity = entity.getUser();

        return Optional.of(new RefreshToken(entity.getId(), entity.getToken(), entity.getExpiryDate(), 
             new User(userEntity.getId(), userEntity.getEmail(), userEntity.getPasswordHash(), userEntity.getRoles()), 
             entity.isRevoked()));
    }
}
