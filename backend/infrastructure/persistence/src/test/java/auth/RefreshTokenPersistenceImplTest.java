package auth;

import com.swiftly.application.auth.port.outbound.RefreshTokenRepository;
import com.swiftly.domain.RefreshToken;
import com.swiftly.domain.User;
import com.swiftly.domain.enums.user.Role;
import com.swiftly.persistence.auth.RefreshTokenPersistenceImpl;
import com.swiftly.persistence.config.PersistenceJpaTestConfig;
import com.swiftly.persistence.entities.RefreshTokenEntity;
import com.swiftly.persistence.entities.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
@ContextConfiguration(classes = PersistenceJpaTestConfig.class)
@Import(RefreshTokenPersistenceImpl.class)
class RefreshTokenPersistenceImplTest {

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private TestEntityManager entityManager;

    private UserEntity user;
    private RefreshTokenEntity refreshTokenEntity;

    @BeforeEach
    void setUp() {
        user = new UserEntity("test@example.com", "passwordHash", List.of(Role.OWNER));
        entityManager.persistAndFlush(user);

        Instant expiryDate = Instant.now().plus(7, ChronoUnit.DAYS);
        refreshTokenEntity = new RefreshTokenEntity("test-token-123", expiryDate, user, false);
        entityManager.persistAndFlush(refreshTokenEntity);
    }

    @Test
    void findByToken_WhenTokenExists_ShouldReturnMappedRefreshToken() {
        RefreshToken found = refreshTokenRepository.findByToken("test-token-123");

        assertThat(found).isNotNull();
        assertThat(found.getId()).isEqualTo(refreshTokenEntity.getId());
        assertThat(found.getToken()).isEqualTo("test-token-123");
        assertThat(found.isRevoked()).isFalse();
        assertThat(found.getUser()).isNotNull();
        assertThat(found.getUser().getId()).isEqualTo(user.getId());
        assertThat(found.getUser().getEmail()).isEqualTo("test@example.com");
    }

    @Test
    void findByToken_WhenTokenDoesNotExist_ShouldReturnNull() {
        RefreshToken found = refreshTokenRepository.findByToken("nonexistent-token");

        assertThat(found).isNull();
    }

    @Test
    void findByToken_WhenTokenCaseDifferent_ShouldReturnNull() {
        RefreshToken found = refreshTokenRepository.findByToken("TEST-TOKEN-123");

        assertThat(found).isNull();
    }

    @Test
    void findByUserId_WhenTokenExists_ShouldReturnMappedRefreshToken() {
        Optional<RefreshToken> found = refreshTokenRepository.findByUserId(user.getId());

        assertThat(found).isPresent();
        RefreshToken token = found.get();
        assertThat(token.getId()).isEqualTo(refreshTokenEntity.getId());
        assertThat(token.getToken()).isEqualTo("test-token-123");
        assertThat(token.getUser().getId()).isEqualTo(user.getId());
    }

    @Test
    void findByUserId_WhenNoTokenExists_ShouldReturnEmpty() {
        UserEntity newUser = new UserEntity("newuser@example.com", "passwordHash", List.of(Role.RENTER));
        entityManager.persistAndFlush(newUser);

        Optional<RefreshToken> found = refreshTokenRepository.findByUserId(newUser.getId());

        assertThat(found).isEmpty();
    }

    @Test
    void save_ShouldPersistRefreshToken() {
        Instant expiryDate = Instant.now().plus(14, ChronoUnit.DAYS);
        User userDomain = new User(user.getId(), user.getEmail(), user.getPasswordHash(), user.getRoles());
        RefreshToken refreshToken = new RefreshToken(null, "new-token-456", expiryDate, userDomain, false);

        RefreshToken saved = refreshTokenRepository.save(refreshToken);

        assertThat(saved).isNotNull();
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getToken()).isEqualTo("new-token-456");
        assertThat(saved.isRevoked()).isFalse();
        assertThat(saved.getUser().getId()).isEqualTo(user.getId());

        RefreshTokenEntity entity = entityManager.find(RefreshTokenEntity.class, saved.getId());
        assertThat(entity).isNotNull();
        assertThat(entity.getToken()).isEqualTo("new-token-456");
        assertThat(entity.isRevoked()).isFalse();
        assertThat(entity.getExpiryDate()).isEqualTo(expiryDate);
        assertThat(entity.getUser().getId()).isEqualTo(user.getId());
    }

    @Test
    void save_WithRevokedTrue_ShouldPersistRevokedStatus() {
        Instant expiryDate = Instant.now().plus(14, ChronoUnit.DAYS);
        User userDomain = new User(user.getId(), user.getEmail(), user.getPasswordHash(), user.getRoles());
        RefreshToken refreshToken = new RefreshToken(null, "revoked-token", expiryDate, userDomain, true);

        RefreshToken saved = refreshTokenRepository.save(refreshToken);

        assertThat(saved.isRevoked()).isTrue();

        RefreshTokenEntity entity = entityManager.find(RefreshTokenEntity.class, saved.getId());
        assertThat(entity).isNotNull();
        assertThat(entity.getToken()).isEqualTo("revoked-token");
        assertThat(entity.isRevoked()).isTrue();
        assertThat(entity.getExpiryDate()).isEqualTo(expiryDate);
        assertThat(entity.getUser().getId()).isEqualTo(user.getId());
    }

    @Test
    void save_WhenUserNotFound_ShouldThrowIllegalArgumentException() {
        User nonExistentUser = new User(999, "nonexistent@example.com", "passwordHash", List.of(Role.RENTER));
        Instant expiryDate = Instant.now().plus(14, ChronoUnit.DAYS);
        RefreshToken refreshToken = new RefreshToken(null, "token", expiryDate, nonExistentUser, false);

        assertThatThrownBy(() -> refreshTokenRepository.save(refreshToken))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("User not found for refresh token");
    }

    @Test
    void save_WithId_ShouldUpdateRefreshToken() {
        User userDomain = new User(user.getId(), user.getEmail(), user.getPasswordHash(), user.getRoles());
        Instant newExpiryDate = Instant.now().plus(30, ChronoUnit.DAYS);
        RefreshToken updatedToken = new RefreshToken(refreshTokenEntity.getId(), "updated-token", newExpiryDate, userDomain, true);

        RefreshToken saved = refreshTokenRepository.save(updatedToken);

        assertThat(saved.getId()).isEqualTo(refreshTokenEntity.getId());
        assertThat(saved.getToken()).isEqualTo("updated-token");
        assertThat(saved.isRevoked()).isTrue();
    }

    @Test
    void deleteById_ShouldDeleteRefreshToken() {
        Integer userId = user.getId();
        refreshTokenRepository.deleteById(userId);
        entityManager.flush();

        assertThat(entityManager.find(RefreshTokenEntity.class, refreshTokenEntity.getId())).isNull();
    }

    @Test
    void delete_WithId_ShouldDeleteRefreshToken() {
        User userDomain = new User(user.getId(), user.getEmail(), user.getPasswordHash(), user.getRoles());
        RefreshToken tokenToDelete = new RefreshToken(refreshTokenEntity.getId(), refreshTokenEntity.getToken(), refreshTokenEntity.getExpiryDate(), userDomain, false);

        refreshTokenRepository.delete(tokenToDelete);
        entityManager.flush();

        assertThat(entityManager.find(RefreshTokenEntity.class, refreshTokenEntity.getId())).isNull();
    }

    @Test
    void delete_WithToken_ShouldDeleteRefreshToken() {
        RefreshToken tokenToDelete = new RefreshToken(null, "test-token-123", refreshTokenEntity.getExpiryDate(), null, false);

        refreshTokenRepository.delete(tokenToDelete);
        entityManager.flush();

        assertThat(entityManager.find(RefreshTokenEntity.class, refreshTokenEntity.getId())).isNull();
    }
}
