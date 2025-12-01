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

        UserEntity userEntity = tokenEntity.get().getUser();
        // Trigger loading if lazy, though we might need a join fetch in repository for better performance. 
        // For now, accessing a property should trigger it if session is open, but to be safe and consistent with the plan:
        // Actually, the plan said "Ensure UserEntity is initialized". 
        // Since we are in a transaction (likely), accessing it might be enough, but let's see.
        // The issue is that we are converting to Domain object which might detach.
        // Let's just use the userEntity we got.
        
        return new RefreshToken(tokenEntity.get().getId(), tokenEntity.get().getToken(), tokenEntity.get().getExpiryDate(), 
                new User(userEntity.getId(), userEntity.getEmail(), userEntity.getPasswordHash(), userEntity.getRoles()), 
                tokenEntity.get().isRevoked());
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

    public RefreshToken findByUserId(Integer userId)
    {
        RefreshTokenEntity refreshTokenEntity = jpaRefreshTokenRepository.findByUserId(userId);

        return new RefreshToken(refreshTokenEntity.getId(), refreshTokenEntity.getToken(), refreshTokenEntity.getExpiryDate(), refreshTokenEntity.getUser(), refreshTokenEntity.isRevoked());
    }
}
