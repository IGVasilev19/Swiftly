package com.swiftly.application.auth;

import com.swiftly.application.auth.dto.RegisterCommand;
import com.swiftly.application.auth.port.outbound.RegisterPort;
import com.swiftly.application.helpers.PasswordHasher;
import com.swiftly.application.user.port.inbound.UserUseCase;
import com.swiftly.domain.Profile;
import com.swiftly.domain.User;
import com.swiftly.domain.enums.user.Role;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
@ActiveProfiles("unit")
class RegisterServiceTest {

    @Mock
    private RegisterPort registerPort;

    @Mock
    private UserUseCase userUseCase;

    @InjectMocks
    private RegisterService registerService;

    @Test
    void register_successfullyRegistersNewUser() {
        // Arrange
        User user = new User(
                "newuser@example.com",
                "StrongPassword123!",
                Role.OWNER
        );
        
        Profile profile = new Profile(
                "Ada Lovelace",
                "+31612345678",
                "123 Babbage Street",
                "London",
                "UK",
                "EC1A 1BB"
        );
        
        RegisterCommand command = new RegisterCommand(user, profile);

        when(userUseCase.existsByEmail(user.getEmail())).thenReturn(false);

        User savedUser = new User(
                user.getEmail(),
                PasswordHasher.hashPassword(user.getPasswordHash()),
                user.getRole()
        );

        Profile savedProfile = new Profile(
                "Ada Lovelace",
                "+31612345678",
                "123 Babbage Street",
                "London",
                "UK",
                "EC1A 1BB"
        );
        savedUser.attachProfile(savedProfile);

        when(registerPort.saveNewUserAndProfile(any(User.class), any(Profile.class)))
                .thenReturn(savedUser);

        // Act
        User result = registerService.register(command);

        // Assert
        assertNotNull(result);
        assertEquals(user.getEmail(), result.getEmail());
        assertEquals(user.getRole(), result.getRole());
        assertNotNull(result.getProfile());
        assertEquals(profile.getFullName(), result.getProfile().getFullName());

        verify(userUseCase).existsByEmail(user.getEmail());
        verify(registerPort).saveNewUserAndProfile(any(User.class), any(Profile.class));
    }

    @Test
    void register_throwsWhenUserAlreadyExists() {
        // Arrange
        User user = new User(
                "existing@example.com",
                "@password3123W",
                Role.RENTER
        );
        
        Profile profile = new Profile(
                "Grace Hopper",
                "+31698765432",
                "Codebreaker Lane",
                "Bletchley",
                "UK",
                "0213Usd"
        );
        
        RegisterCommand command = new RegisterCommand(user, profile);

        when(userUseCase.existsByEmail(user.getEmail())).thenReturn(true);

        // Act + Assert
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> registerService.register(command));

        assertEquals("User already exists", ex.getMessage());

        verify(userUseCase).existsByEmail(user.getEmail());
        verify(registerPort, never()).saveNewUserAndProfile(any(), any());
    }
}
