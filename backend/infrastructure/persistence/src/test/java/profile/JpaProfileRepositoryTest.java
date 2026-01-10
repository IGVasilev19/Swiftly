package profile;

import com.swiftly.domain.enums.user.Role;
import com.swiftly.persistence.config.PersistenceJpaTestConfig;
import com.swiftly.persistence.entities.ProfileEntity;
import com.swiftly.persistence.entities.UserEntity;
import com.swiftly.persistence.profile.JpaProfileRepository;
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
class JpaProfileRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private JpaProfileRepository profileRepository;

    private UserEntity user;
    private ProfileEntity profile;

    @BeforeEach
    void setUp() {
        user = new UserEntity("test@example.com", "passwordHash", List.of(Role.OWNER));
        entityManager.persistAndFlush(user);
        profile = new ProfileEntity(user, "Test User", "+1234567890", "https://example.com/avatar.jpg");
        entityManager.persistAndFlush(profile);
    }

    @Test
    void findById_WhenProfileExists_ShouldReturnProfile() {

        Optional<ProfileEntity> found = profileRepository.findById(profile.getId());

        assertThat(found).isPresent();
        assertThat(found.get().getId()).isEqualTo(profile.getId());
        assertThat(found.get().getFullName()).isEqualTo("Test User");
        assertThat(found.get().getPhone()).isEqualTo("+1234567890");
    }

    @Test
    void findById_WhenProfileDoesNotExist_ShouldReturnEmpty() {

        Optional<ProfileEntity> found = profileRepository.findById(999);

        assertThat(found).isEmpty();
    }

    @Test
    void findById_ShouldReturnProfileWithUser() {

        Optional<ProfileEntity> found = profileRepository.findById(profile.getId());

        assertThat(found).isPresent();
        assertThat(found.get().getUser()).isNotNull();
        assertThat(found.get().getUser().getId()).isEqualTo(user.getId());
        assertThat(found.get().getUser().getEmail()).isEqualTo("test@example.com");
    }

    @Test
    void save_ShouldPersistProfile() {

        UserEntity newUser = new UserEntity("newuser@example.com", "passwordHash", List.of(Role.RENTER));
        entityManager.persistAndFlush(newUser);

        ProfileEntity newProfile = new ProfileEntity(newUser, "New User", "+9876543210", null);

        ProfileEntity saved = profileRepository.save(newProfile);
        entityManager.flush();

        assertThat(saved.getId()).isNotNull();
        assertThat(entityManager.find(ProfileEntity.class, saved.getId())).isNotNull();
        assertThat(saved.getFullName()).isEqualTo("New User");
        assertThat(saved.getPhone()).isEqualTo("+9876543210");
    }

    @Test
    void save_WithAvatarUrl_ShouldPersistAvatarUrl() {

        UserEntity newUser = new UserEntity("avataruser@example.com", "passwordHash", List.of(Role.RENTER));
        entityManager.persistAndFlush(newUser);

        ProfileEntity newProfile = new ProfileEntity(newUser, "Avatar User", "+1111111111", "https://example.com/newavatar.jpg");

        ProfileEntity saved = profileRepository.save(newProfile);
        entityManager.flush();

        assertThat(saved.getAvatarUrl()).isEqualTo("https://example.com/newavatar.jpg");
    }

    @Test
    void save_WithNullAvatarUrl_ShouldPersistNull() {

        UserEntity newUser = new UserEntity("nullavatar@example.com", "passwordHash", List.of(Role.RENTER));
        entityManager.persistAndFlush(newUser);

        ProfileEntity newProfile = new ProfileEntity(newUser, "Null Avatar User", "+2222222222", null);

        ProfileEntity saved = profileRepository.save(newProfile);
        entityManager.flush();

        assertThat(saved.getAvatarUrl()).isNull();
    }

    @Test
    void findAll_ShouldReturnAllProfiles() {

        UserEntity user2 = new UserEntity("user2@example.com", "passwordHash2", List.of(Role.RENTER));
        entityManager.persistAndFlush(user2);
        ProfileEntity profile2 = new ProfileEntity(user2, "User 2", "+3333333333", null);
        entityManager.persistAndFlush(profile2);

        List<ProfileEntity> all = profileRepository.findAll();

        assertThat(all).hasSize(2);
        assertThat(all).extracting(ProfileEntity::getId).containsExactlyInAnyOrder(profile.getId(), profile2.getId());
    }

    @Test
    void deleteById_ShouldDeleteProfile() {

        Integer profileId = profile.getId();

        profileRepository.deleteById(profileId);
        entityManager.flush();

        assertThat(entityManager.find(ProfileEntity.class, profileId)).isNull();
    }

    @Test
    void update_ShouldUpdateProfile() {

        profile.setFullName("Updated Name");
        profile.setPhone("+9999999999");
        profile.setAvatarUrl("https://example.com/updated.jpg");

        ProfileEntity updated = profileRepository.save(profile);
        entityManager.flush();
        entityManager.clear();

        Optional<ProfileEntity> found = profileRepository.findById(updated.getId());
        assertThat(found).isPresent();
        assertThat(found.get().getFullName()).isEqualTo("Updated Name");
        assertThat(found.get().getPhone()).isEqualTo("+9999999999");
        assertThat(found.get().getAvatarUrl()).isEqualTo("https://example.com/updated.jpg");
    }
}
