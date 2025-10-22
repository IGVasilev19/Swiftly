package com.swiftly.application.auth;

import com.swiftly.application.auth.RegisterService;
import com.swiftly.application.auth.dto.RegisterCommand;
import com.swiftly.application.auth.port.inbound.RegisterUseCase;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RegisterServiceTest {

    @Mock
    private RegisterUseCase registerUseCase;

    @Mock
    private UserUseCase userUseCase;

    @InjectMocks
    private RegisterService registerService;

    @Test
    void register_successfullyRegistersNewUser() {
        // Arrange
        RegisterCommand command = new RegisterCommand(
                "newuser@example.com",
                "@strongPassword123",
                "USER",
                "Ada Lovelace",
                Role.OWNER,
                "Analytical Engine St",
                "London",
                "Netherlands",
                "2131412"
        );

        when(userUseCase.existsByEmail(command.email())).thenReturn(false);

        // Fake a saved user for return
        User savedUser = new User(
                command.email(),
                PasswordHasher.hashPassword(command.password()),
                command.role(),
                false
        );

        // We don't test PasswordHasher logic here — assume it's static and tested elsewhere
        when(registerUseCase.register(command))
                .thenReturn(savedUser);

        // Act
        User result = registerService.register(command);

        // Assert
        assertNotNull(result);
        assertEquals(command.email(), result.getEmail());
        assertEquals(command.role(), result.getRole());
        assertFalse(result.getStatus());

        verify(userUseCase).existsByEmail(command.email());
        verify(registerUseCase).register(command);
    }

    @Test
    void register_throwsWhenUserAlreadyExists() {
        // Arrange
        RegisterCommand command = new RegisterCommand(
                "existing@example.com",
                "@password3123W",
                "USER",
                "Alan Turing",
                Role.RENTER,
                "Codebreaker Lane",
                "Bletchley",
                "67890",
                "0213Usd"
        );

        when(userUseCase.existsByEmail(command.email())).thenReturn(true);

        // Act + Assert
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> registerService.register(command));

        assertEquals("User already exists", ex.getMessage());

        verify(userUseCase).existsByEmail(command.email());
        verify(registerUseCase, never()).register(command);
    }
}
