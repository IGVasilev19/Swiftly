package auth;

import com.swiftly.domain.enums.user.Role;
import com.swiftly.persistence.auth.JpaRefreshTokenRepository;
import com.swiftly.persistence.config.PersistenceJpaTestConfig;
import com.swiftly.persistence.entities.RefreshTokenEntity;
import com.swiftly.persistence.entities.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ContextConfiguration;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ContextConfiguration(classes = PersistenceJpaTestConfig.class)
class JpaRefreshTokenRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private JpaRefreshTokenRepository refreshTokenRepository;

    private UserEntity user;
    private RefreshTokenEntity refreshToken;

    @BeforeEach
    void setUp() {
        user = new UserEntity("test@example.com", "passwordHash", List.of(Role.OWNER));
        entityManager.persistAndFlush(user);

        Instant expiryDate = Instant.now().plus(7, ChronoUnit.DAYS);
        refreshToken = new RefreshTokenEntity("test-token-123", expiryDate, user, false);
        entityManager.persistAndFlush(refreshToken);
    }

    @Test
    void findByToken_WhenTokenExists_ShouldReturnRefreshToken() {

        Optional<RefreshTokenEntity> found = refreshTokenRepository.findByToken("test-token-123");

        assertThat(found).isPresent();
        assertThat(found.get().getId()).isEqualTo(refreshToken.getId());
        assertThat(found.get().getToken()).isEqualTo("test-token-123");
        assertThat(found.get().getUser().getId()).isEqualTo(user.getId());
    }

    @Test
    void findByToken_WhenTokenDoesNotExist_ShouldReturnEmpty() {

        Optional<RefreshTokenEntity> found = refreshTokenRepository.findByToken("nonexistent-token");

        assertThat(found).isEmpty();
    }

    @Test
    void findByToken_WhenTokenCaseDifferent_ShouldReturnEmpty() {

        Optional<RefreshTokenEntity> found = refreshTokenRepository.findByToken("TEST-TOKEN-123");

        assertThat(found).isEmpty();
    }

    @Test
    void findByUserId_WhenTokenExists_ShouldReturnRefreshToken() {

        Optional<RefreshTokenEntity> found = refreshTokenRepository.findByUserId(user.getId());

        assertThat(found).isPresent();
        assertThat(found.get().getId()).isEqualTo(refreshToken.getId());
        assertThat(found.get().getUser().getId()).isEqualTo(user.getId());
    }

    @Test
    void findByUserId_WhenNoTokenExists_ShouldReturnEmpty() {

        UserEntity user2 = new UserEntity("user2@example.com", "passwordHash2", List.of(Role.RENTER));
        entityManager.persistAndFlush(user2);

        Optional<RefreshTokenEntity> found = refreshTokenRepository.findByUserId(user2.getId());

        assertThat(found).isEmpty();
    }

    @Test
    void findByUserId_WhenMultipleTokensExist_ShouldThrowException() {

        Instant expiryDate2 = Instant.now().plus(14, ChronoUnit.DAYS);
        RefreshTokenEntity token2 = new RefreshTokenEntity("test-token-456", expiryDate2, user, false);
        entityManager.persistAndFlush(token2);

        org.assertj.core.api.Assertions.assertThatThrownBy(() -> {
            refreshTokenRepository.findByUserId(user.getId());
        }).isInstanceOf(org.springframework.dao.IncorrectResultSizeDataAccessException.class);
    }

    @Test
    void deleteByUserId_ShouldDeleteAllTokensForUser() {

        Instant expiryDate2 = Instant.now().plus(14, ChronoUnit.DAYS);
        RefreshTokenEntity token2 = new RefreshTokenEntity("test-token-456", expiryDate2, user, false);
        entityManager.persistAndFlush(token2);

        refreshTokenRepository.deleteByUserId(user.getId());
        entityManager.flush();

        assertThat(entityManager.find(RefreshTokenEntity.class, refreshToken.getId())).isNull();
        assertThat(entityManager.find(RefreshTokenEntity.class, token2.getId())).isNull();
    }

    @Test
    void deleteByUserId_WhenNoTokensExist_ShouldNotThrowException() {

        UserEntity user2 = new UserEntity("user2@example.com", "passwordHash2", List.of(Role.RENTER));
        entityManager.persistAndFlush(user2);

        refreshTokenRepository.deleteByUserId(user2.getId());
        entityManager.flush();
    }

    @Test
    void deleteByUserId_ShouldOnlyDeleteTokensForSpecificUser() {

        UserEntity user2 = new UserEntity("user2@example.com", "passwordHash2", List.of(Role.RENTER));
        entityManager.persistAndFlush(user2);

        Instant expiryDate2 = Instant.now().plus(7, ChronoUnit.DAYS);
        RefreshTokenEntity token2 = new RefreshTokenEntity("user2-token", expiryDate2, user2, false);
        entityManager.persistAndFlush(token2);

        refreshTokenRepository.deleteByUserId(user.getId());
        entityManager.flush();

        assertThat(entityManager.find(RefreshTokenEntity.class, refreshToken.getId())).isNull();
        assertThat(entityManager.find(RefreshTokenEntity.class, token2.getId())).isNotNull();
    }

    @Test
    void save_ShouldPersistRefreshToken() {

        UserEntity user2 = new UserEntity("newuser@example.com", "passwordHash", List.of(Role.RENTER));
        entityManager.persistAndFlush(user2);

        Instant expiryDate = Instant.now().plus(7, ChronoUnit.DAYS);
        RefreshTokenEntity newToken = new RefreshTokenEntity("new-token-789", expiryDate, user2, false);

        RefreshTokenEntity saved = refreshTokenRepository.save(newToken);
        entityManager.flush();

        assertThat(saved.getId()).isNotNull();
        assertThat(entityManager.find(RefreshTokenEntity.class, saved.getId())).isNotNull();
        assertThat(saved.getToken()).isEqualTo("new-token-789");
    }

    @Test
    void save_WithRevokedTrue_ShouldPersistRevokedStatus() {

        UserEntity user2 = new UserEntity("revokeduser@example.com", "passwordHash", List.of(Role.RENTER));
        entityManager.persistAndFlush(user2);

        Instant expiryDate = Instant.now().plus(7, ChronoUnit.DAYS);
        RefreshTokenEntity revokedToken = new RefreshTokenEntity("revoked-token", expiryDate, user2, true);

        RefreshTokenEntity saved = refreshTokenRepository.save(revokedToken);
        entityManager.flush();

        assertThat(saved.isRevoked()).isTrue();
    }

    @Test
    void save_UpdateRevokedStatus_ShouldUpdateToken() {

        assertThat(refreshToken.isRevoked()).isFalse();

        refreshToken.setRevoked(true);
        RefreshTokenEntity updated = refreshTokenRepository.save(refreshToken);
        entityManager.flush();
        entityManager.clear();

        Optional<RefreshTokenEntity> found = refreshTokenRepository.findById(updated.getId());
        assertThat(found).isPresent();
        assertThat(found.get().isRevoked()).isTrue();
    }

    @Test
    void findAll_ShouldReturnAllRefreshTokens() {

        UserEntity user2 = new UserEntity("user2@example.com", "passwordHash2", List.of(Role.RENTER));
        entityManager.persistAndFlush(user2);

        Instant expiryDate2 = Instant.now().plus(7, ChronoUnit.DAYS);
        RefreshTokenEntity token2 = new RefreshTokenEntity("token-2", expiryDate2, user2, false);
        entityManager.persistAndFlush(token2);

        List<RefreshTokenEntity> all = refreshTokenRepository.findAll();

        assertThat(all).hasSize(2);
        assertThat(all).extracting(RefreshTokenEntity::getId).containsExactlyInAnyOrder(refreshToken.getId(), token2.getId());
    }

    @Test
    void deleteById_ShouldDeleteRefreshToken() {

        Long tokenId = refreshToken.getId();

        refreshTokenRepository.deleteById(tokenId);
        entityManager.flush();

        assertThat(entityManager.find(RefreshTokenEntity.class, tokenId)).isNull();
    }

    @Test
    void findById_WhenTokenExists_ShouldReturnToken() {

        Optional<RefreshTokenEntity> found = refreshTokenRepository.findById(refreshToken.getId());

        assertThat(found).isPresent();
        assertThat(found.get().getId()).isEqualTo(refreshToken.getId());
        assertThat(found.get().getToken()).isEqualTo("test-token-123");
    }

    @Test
    void findById_WhenTokenDoesNotExist_ShouldReturnEmpty() {

        Optional<RefreshTokenEntity> found = refreshTokenRepository.findById(999L);

        assertThat(found).isEmpty();
    }
}
