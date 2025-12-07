package com.swiftly.application.auth;

import com.swiftly.application.auth.dto.RegisterCommand;
import com.swiftly.application.auth.port.outbound.RegisterRepository;
import com.swiftly.application.helpers.PasswordHasher;
import com.swiftly.application.user.port.inbound.UserService;
import com.swiftly.domain.Profile;
import com.swiftly.domain.User;
import com.swiftly.domain.enums.user.Role;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("unit")
public class RegisterServiceImplTest {

    @Mock
    private RegisterRepository registerRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private RegisterServiceImpl registerServiceImpl;

    @Test
    void register_successfullyRegistersNewUser() {
        List<Role> roles = List.of(Role.OWNER);

        User user = new User(
                "newuser@example.com",
                "StrongPassword123!",
                roles
        );
        
        Profile profile = new Profile(
                "Ada Lovelace",
                "+31612345678"
        );
        
        RegisterCommand command = new RegisterCommand(user, profile);

        when(userService.existsByEmail(user.getEmail())).thenReturn(false);

        User savedUser = new User(
                user.getEmail(),
                PasswordHasher.hashPassword(user.getPasswordHash()),
                user.getRoles()
        );

        Profile savedProfile = new Profile(
                "Ada Lovelace",
                "+31612345678"
        );
        savedUser.attachProfile(savedProfile);

        when(registerRepository.saveNewUserAndProfile(any(User.class), any(Profile.class)))
                .thenReturn(savedUser);

        User result = registerServiceImpl.register(command);

        assertNotNull(result);
        assertEquals(user.getEmail(), result.getEmail());
        assertEquals(user.getRoles(), result.getRoles());
        assertNotNull(result.getProfile());
        assertEquals(profile.getFullName(), result.getProfile().getFullName());

        verify(userService).existsByEmail(user.getEmail());
        verify(registerRepository).saveNewUserAndProfile(any(User.class), any(Profile.class));
    }

    @Test
    void register_throwsWhenUserAlreadyExists() {
        List<Role> roles = List.of(Role.RENTER);

        User user = new User(
                "existing@example.com",
                "@password3123W",
                roles
        );
        
        Profile profile = new Profile(
                "Grace Hopper",
                "+31698765432"
        );
        
        RegisterCommand command = new RegisterCommand(user, profile);

        when(userService.existsByEmail(user.getEmail())).thenReturn(true);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> registerServiceImpl.register(command));

        assertEquals("User already exists", ex.getMessage());

        verify(userService).existsByEmail(user.getEmail());
        verify(registerRepository, never()).saveNewUserAndProfile(any(), any());
    }
}
