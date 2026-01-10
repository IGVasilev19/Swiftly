package user;

import com.swiftly.domain.enums.user.Role;
import com.swiftly.persistence.config.PersistenceJpaTestConfig;
import com.swiftly.persistence.entities.ProfileEntity;
import com.swiftly.persistence.entities.UserEntity;
import com.swiftly.persistence.user.JpaUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ContextConfiguration(classes = PersistenceJpaTestConfig.class)
class JpaUserRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private JpaUserRepository userRepository;

    private UserEntity user;

    @BeforeEach
    void setUp() {
        user = new UserEntity("test@example.com", "passwordHash", List.of(Role.OWNER, Role.RENTER));
        entityManager.persistAndFlush(user);
    }

    @Test
    void existsByEmail_WhenEmailExists_ShouldReturnTrue() {

        boolean exists = userRepository.existsByEmail("test@example.com");

        assertThat(exists).isTrue();
    }

    @Test
    void existsByEmail_WhenEmailDoesNotExist_ShouldReturnFalse() {

        boolean exists = userRepository.existsByEmail("nonexistent@example.com");

        assertThat(exists).isFalse();
    }

    @Test
    void existsByEmail_WhenEmailCaseDifferent_ShouldReturnFalse() {

        boolean exists = userRepository.existsByEmail("TEST@EXAMPLE.COM");

        assertThat(exists).isFalse();
    }

    @Test
    void findByEmail_WhenEmailExists_ShouldReturnUserWithRoles() {

        Optional<UserEntity> found = userRepository.findByEmail("test@example.com");

        assertThat(found).isPresent();
        assertThat(found.get().getId()).isEqualTo(user.getId());
        assertThat(found.get().getEmail()).isEqualTo("test@example.com");
        assertThat(found.get().getRoles()).isNotNull();
        assertThat(found.get().getRoles()).containsExactlyInAnyOrder(Role.OWNER, Role.RENTER);
    }

    @Test
    void findByEmail_WhenEmailDoesNotExist_ShouldReturnEmpty() {

        Optional<UserEntity> found = userRepository.findByEmail("nonexistent@example.com");

        assertThat(found).isEmpty();
    }

    @Test
    void findByEmail_WhenUserHasProfile_ShouldStillReturnUser() {

        ProfileEntity profile = new ProfileEntity(user, "Test User", "+1234567890", null);
        entityManager.persistAndFlush(profile);
        entityManager.clear();

        Optional<UserEntity> found = userRepository.findByEmail("test@example.com");

        assertThat(found).isPresent();
        assertThat(found.get().getId()).isEqualTo(user.getId());
    }

    @Test
    void findById_WhenUserExists_ShouldReturnUser() {

        Optional<UserEntity> found = userRepository.findById(user.getId());

        assertThat(found).isPresent();
        assertThat(found.get().getId()).isEqualTo(user.getId());
        assertThat(found.get().getEmail()).isEqualTo("test@example.com");
    }

    @Test
    void findById_WhenUserDoesNotExist_ShouldReturnEmpty() {

        Optional<UserEntity> found = userRepository.findById(999);

        assertThat(found).isEmpty();
    }

    @Test
    void save_ShouldPersistUser() {

        UserEntity newUser = new UserEntity("newuser@example.com", "newPasswordHash", List.of(Role.RENTER));

        UserEntity saved = userRepository.save(newUser);
        entityManager.flush();

        assertThat(saved.getId()).isNotNull();
        assertThat(entityManager.find(UserEntity.class, saved.getId())).isNotNull();
        assertThat(saved.getEmail()).isEqualTo("newuser@example.com");
    }

    @Test
    void save_WithMultipleRoles_ShouldPersistAllRoles() {

        UserEntity newUser = new UserEntity("multirole@example.com", "passwordHash", List.of(Role.OWNER, Role.RENTER));

        UserEntity saved = userRepository.save(newUser);
        entityManager.flush();
        entityManager.clear();

        Optional<UserEntity> found = userRepository.findById(saved.getId());
        assertThat(found).isPresent();
        assertThat(found.get().getRoles()).hasSize(2);
        assertThat(found.get().getRoles()).containsExactlyInAnyOrder(Role.OWNER, Role.RENTER);
    }

    @Test
    void findAll_ShouldReturnAllUsers() {

        UserEntity user2 = new UserEntity("user2@example.com", "passwordHash2", List.of(Role.RENTER));
        entityManager.persistAndFlush(user2);

        List<UserEntity> all = userRepository.findAll();

        assertThat(all).hasSize(2);
        assertThat(all).extracting(UserEntity::getId).containsExactlyInAnyOrder(user.getId(), user2.getId());
    }

    @Test
    void deleteById_ShouldDeleteUser() {

        Integer userId = user.getId();

        userRepository.deleteById(userId);
        entityManager.flush();

        assertThat(entityManager.find(UserEntity.class, userId)).isNull();
    }
}
