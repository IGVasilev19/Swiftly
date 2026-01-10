package user;

import com.swiftly.application.user.port.outbound.UserRepository;
import com.swiftly.domain.User;
import com.swiftly.domain.enums.user.Role;
import com.swiftly.persistence.config.PersistenceJpaTestConfig;
import com.swiftly.persistence.entities.ProfileEntity;
import com.swiftly.persistence.entities.UserEntity;
import com.swiftly.persistence.user.UserPersistenceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ContextConfiguration(classes = PersistenceJpaTestConfig.class)
@Import(UserPersistenceImpl.class)
class UserPersistenceImplTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TestEntityManager entityManager;

    private UserEntity userEntity;

    @BeforeEach
    void setUp() {
        userEntity = new UserEntity("test@example.com", "passwordHash", List.of(Role.OWNER, Role.RENTER));
        entityManager.persistAndFlush(userEntity);
    }

    @Test
    void existsByEmail_WhenEmailExists_ShouldReturnTrue() {
        Boolean exists = userRepository.existsByEmail("test@example.com");

        assertThat(exists).isTrue();
    }

    @Test
    void existsByEmail_WhenEmailDoesNotExist_ShouldReturnFalse() {
        Boolean exists = userRepository.existsByEmail("nonexistent@example.com");

        assertThat(exists).isFalse();
    }

    @Test
    void existsByEmail_WhenEmailCaseDifferent_ShouldReturnFalse() {
        Boolean exists = userRepository.existsByEmail("TEST@EXAMPLE.COM");

        assertThat(exists).isFalse();
    }

    @Test
    void findByEmail_WhenEmailExists_ShouldReturnMappedUser() {
        Optional<User> found = userRepository.findByEmail("test@example.com");

        assertThat(found).isPresent();
        User user = found.get();
        assertThat(user.getId()).isEqualTo(userEntity.getId());
        assertThat(user.getEmail()).isEqualTo("test@example.com");
        assertThat(user.getPasswordHash()).isEqualTo("passwordHash");
        assertThat(user.getRoles()).containsExactlyInAnyOrder(Role.OWNER, Role.RENTER);
    }

    @Test
    void findByEmail_WhenEmailDoesNotExist_ShouldReturnEmpty() {
        Optional<User> found = userRepository.findByEmail("nonexistent@example.com");

        assertThat(found).isEmpty();
    }

    @Test
    void save_WhenNewUser_ShouldPersistAndReturnMappedUser() {
        User newUser = new User("newuser@example.com", "newPasswordHash", List.of(Role.RENTER));

        User saved = userRepository.save(newUser);

        assertThat(saved).isNotNull();
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getEmail()).isEqualTo("newuser@example.com");
        assertThat(saved.getPasswordHash()).isEqualTo("newPasswordHash");
        assertThat(saved.getRoles()).containsExactly(Role.RENTER);

        UserEntity entity = entityManager.find(UserEntity.class, saved.getId());
        assertThat(entity).isNotNull();
        assertThat(entity.getEmail()).isEqualTo("newuser@example.com");
    }

    @Test
    void save_WithMultipleRoles_ShouldPersistAllRoles() {
        User newUser = new User("multirole@example.com", "passwordHash", List.of(Role.OWNER, Role.RENTER));

        User saved = userRepository.save(newUser);
        entityManager.flush();
        entityManager.clear();

        Optional<User> found = userRepository.findByEmail("multirole@example.com");
        assertThat(found).isPresent();
        assertThat(found.get().getRoles()).hasSize(2);
        assertThat(found.get().getRoles()).containsExactlyInAnyOrder(Role.OWNER, Role.RENTER);
    }

    @Test
    void findById_WhenUserExists_ShouldReturnMappedUser() {
        User found = userRepository.findById(userEntity.getId());

        assertThat(found).isNotNull();
        assertThat(found.getId()).isEqualTo(userEntity.getId());
        assertThat(found.getEmail()).isEqualTo("test@example.com");
        assertThat(found.getPasswordHash()).isEqualTo("passwordHash");
        assertThat(found.getRoles()).containsExactlyInAnyOrder(Role.OWNER, Role.RENTER);
    }

    @Test
    void findById_WhenUserDoesNotExist_ShouldReturnNull() {
        User found = userRepository.findById(999);

        assertThat(found).isNull();
    }
}
