package com.swiftly.application.auth;

import com.swiftly.application.auth.dto.RegisterCommand;
import com.swiftly.application.auth.port.outbound.RegisterPort;
import com.swiftly.application.helpers.PasswordHasher;
import com.swiftly.application.user.port.inbound.UserUseCase;
import com.swiftly.domain.Profile;
import com.swiftly.domain.User;
import com.swiftly.domain.enums.user.Role;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)

@ExtendWith(MockitoExtension.class)
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
        RegisterCommand command = new RegisterCommand(
                "newuser@example.com",
                "StrongPassword123!",
                "Ada Lovelace",
                "+31612345678",
                Role.OWNER,
                "123 Babbage Street",
                "London",
                "UK",
                "EC1A 1BB"
        );

        when(userUseCase.existsByEmail(command.email())).thenReturn(false);

        User savedUser = new User(
                command.email(),
                PasswordHasher.hashPassword(command.password()),
                command.role(),
                false
        );

        Profile profile = new Profile(
                savedUser,
                command.fullName(),
                command.phoneNumber(),
                command.address(),
                command.city(),
                command.country(),
                command.postalCode()
        );
        savedUser.attachProfile(profile);

        when(registerPort.saveNewUserAndProfile(any(User.class), any(Profile.class)))
                .thenReturn(savedUser);

        // Act
        User result = registerService.register(command);

        // Assert
        assertNotNull(result);
        assertEquals(command.email(), result.getEmail());
        assertEquals(command.role(), result.getRole());
        assertFalse(result.getStatus());
        assertNotNull(result.getProfile());
        assertEquals(command.fullName(), result.getProfile().getFullName());

        verify(userUseCase).existsByEmail(command.email());
        verify(registerPort).saveNewUserAndProfile(any(User.class), any(Profile.class));
    }

    @Test
    void register_throwsWhenUserAlreadyExists() {
        // Arrange
        RegisterCommand command = new RegisterCommand(
                "existing@example.com",
                "@password3123W",
                "Grace Hopper",
                "+31698765432",
                Role.RENTER,
                "Codebreaker Lane",
                "Bletchley",
                "UK",
                "0213Usd"
        );

        when(userUseCase.existsByEmail(command.email())).thenReturn(true);

        // Act + Assert
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> registerService.register(command));

        assertEquals("User already exists", ex.getMessage());

        verify(userUseCase).existsByEmail(command.email());
        verify(registerPort, never()).saveNewUserAndProfile(any(), any());
    }
}
