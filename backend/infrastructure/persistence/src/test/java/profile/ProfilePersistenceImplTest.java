package profile;

import com.swiftly.application.profile.port.outbound.ProfileRepository;
import com.swiftly.domain.Profile;
import com.swiftly.domain.User;
import com.swiftly.domain.enums.user.Role;
import com.swiftly.persistence.config.PersistenceJpaTestConfig;
import com.swiftly.persistence.entities.ProfileEntity;
import com.swiftly.persistence.entities.UserEntity;
import com.swiftly.persistence.profile.ProfilePersistenceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
@ContextConfiguration(classes = PersistenceJpaTestConfig.class)
@Import(ProfilePersistenceImpl.class)
class ProfilePersistenceImplTest {

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private TestEntityManager entityManager;

    private UserEntity userEntity;
    private ProfileEntity profileEntity;
    private User userDomain;

    @BeforeEach
    void setUp() {
        userEntity = new UserEntity("test@example.com", "passwordHash", List.of(Role.OWNER));
        entityManager.persistAndFlush(userEntity);
        profileEntity = new ProfileEntity(userEntity, "Test User", "+1234567890", "http://example.com/avatar.jpg");
        entityManager.persistAndFlush(profileEntity);
        userDomain = new User(userEntity.getId(), userEntity.getEmail(), userEntity.getPasswordHash(), userEntity.getRoles());
    }

    @Test
    void findById_WhenProfileExists_ShouldReturnMappedProfile() {
        Profile found = profileRepository.findById(profileEntity.getId());

        assertThat(found).isNotNull();
        assertThat(found.getId()).isEqualTo(profileEntity.getId());
        assertThat(found.getFullName()).isEqualTo("Test User");
        assertThat(found.getPhone()).isEqualTo("+1234567890");
        assertThat(found.getAvatarUrl()).isEqualTo("http://example.com/avatar.jpg");
    }

    @Test
    void findById_WhenProfileDoesNotExist_ShouldReturnNull() {
        Profile found = profileRepository.findById(999);

        assertThat(found).isNull();
    }

    @Test
    void save_WhenValidProfile_ShouldPersistAndReturnMappedProfile() {
        User newUser = new User("newuser@example.com", "passwordHash", List.of(Role.RENTER));
        newUser = saveUser(newUser);
        Profile newProfile = new Profile("New User", "+9876543210");
        newProfile.setUser(newUser);

        Profile saved = profileRepository.save(newProfile);

        assertThat(saved).isNotNull();
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getFullName()).isEqualTo("New User");
        assertThat(saved.getPhone()).isEqualTo("+9876543210");

        ProfileEntity entity = entityManager.find(ProfileEntity.class, saved.getId());
        assertThat(entity).isNotNull();
        assertThat(entity.getUser().getId()).isEqualTo(newUser.getId());
        assertThat(entity.getFullName()).isEqualTo("New User");
        assertThat(entity.getPhone()).isEqualTo("+9876543210");
        assertThat(entity.getAvatarUrl()).isNull();
    }

    @Test
    void save_WhenProfileWithId_ShouldUpdateAndReturnMappedProfile() {
        Profile updatedProfile = new Profile(profileEntity.getId(), "Updated Name", "+9999999999", "http://example.com/new-avatar.jpg");
        updatedProfile.setUser(userDomain);

        Profile saved = profileRepository.save(updatedProfile);

        assertThat(saved.getId()).isEqualTo(profileEntity.getId());
        assertThat(saved.getFullName()).isEqualTo("Updated Name");
        assertThat(saved.getPhone()).isEqualTo("+9999999999");
        assertThat(saved.getAvatarUrl()).isEqualTo("http://example.com/new-avatar.jpg");

        ProfileEntity entity = entityManager.find(ProfileEntity.class, saved.getId());
        assertThat(entity.getFullName()).isEqualTo("Updated Name");
    }

    @Test
    void save_WhenProfileWithoutUser_ShouldThrowIllegalArgumentException() {
        Profile profile = new Profile("Test Name", "+1234567890");

        assertThatThrownBy(() -> profileRepository.save(profile))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Profile must have a user with ID");
    }

    @Test
    void save_WhenUserNotFound_ShouldThrowIllegalArgumentException() {
        User nonExistentUser = new User(999);
        Profile profile = new Profile("Test Name", "+1234567890");
        profile.setUser(nonExistentUser);

        assertThatThrownBy(() -> profileRepository.save(profile))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("User not found");
    }

    @Test
    void save_WithNullAvatarUrl_ShouldPersistNull() {
        User newUser = new User("user@example.com", "passwordHash", List.of(Role.RENTER));
        newUser = saveUser(newUser);
        Profile newProfile = new Profile("User Name", "+1111111111");
        newProfile.setUser(newUser);
        newProfile.setAvatarUrl(null);

        Profile saved = profileRepository.save(newProfile);

        assertThat(saved.getAvatarUrl()).isNull();

        ProfileEntity entity = entityManager.find(ProfileEntity.class, saved.getId());
        assertThat(entity.getAvatarUrl()).isNull();
    }

    private User saveUser(User user) {
        UserEntity userEntity = new UserEntity(user.getEmail(), user.getPasswordHash(), user.getRoles());
        UserEntity saved = entityManager.persistFlushFind(userEntity);
        return new User(saved.getId(), saved.getEmail(), saved.getPasswordHash(), saved.getRoles());
    }
}
